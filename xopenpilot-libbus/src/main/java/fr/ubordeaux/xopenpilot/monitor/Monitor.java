package fr.ubordeaux.xopenpilot.monitor;

import fr.ubordeaux.xopenpilot.libbus.client.*;

import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.*;

import javax.json.*;

public class Monitor
{
   private MonitorModel model;

   private int selectedSenderId  = -1;
   private int selectedMessageId = -1;

   private Timer timer;
   
   private JFrame window;
   
   private JSplitPane splitLeft;
   private JSplitPane splitRight;

   private JTabbedPane messageModePane;
   
   private JPanel sendersPanel;
   private JPanel messagesPanel;
   private JPanel selectedMessagePanel;
   private JPanel lastMessagePanel;

   private JScrollPane sendersScroll;
   private JScrollPane messagesScroll;
   private JScrollPane lastMessageScroll;
   private JScrollPane selectedMessageScroll;
   
   
   public void start(String hostname)
   {
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               try {
                  start_(hostname);
               }
               catch (IOException e) {
                  throw new RuntimeException(e);
               }
            }});
   }
   
   private void start_(String hostname) throws IOException
   {
      stop_();
      
      model = new MonitorModel(hostname);
      
      window = new JFrame("bus monitor");
      window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

      window.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent windowEvent) {
               try {
                  stop_();
               }
               catch (IOException e) {
                  throw new RuntimeException(e);
               }
            }});

      sendersPanel         = new JPanel();
      messagesPanel        = new JPanel();
      selectedMessagePanel = new JPanel();
      lastMessagePanel     = new JPanel();
      
      splitLeft  = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

      messageModePane = new JTabbedPane();

      sendersPanel        .setLayout(new BoxLayout(sendersPanel        , BoxLayout.Y_AXIS));
      messagesPanel       .setLayout(new BoxLayout(messagesPanel       , BoxLayout.Y_AXIS));
      selectedMessagePanel.setLayout(new BoxLayout(selectedMessagePanel, BoxLayout.Y_AXIS));
      lastMessagePanel    .setLayout(new BoxLayout(lastMessagePanel    , BoxLayout.Y_AXIS));
            
      splitLeft.setLeftComponent(sendersPanel);
      splitLeft.setRightComponent(messageModePane);

      splitRight.setLeftComponent(messagesPanel);
      splitRight.setRightComponent(selectedMessagePanel);
      
      messageModePane.addTab("message history", splitRight);
      messageModePane.addTab("last message only", lastMessagePanel);

      window.getContentPane().add(splitLeft);

      refresh();
      
      window.pack();
      window.setVisible(true);

      timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               refresh();
            }});

      timer.start();
   }
   
   private void refresh()
   {
      try {
         model.refresh(selectedSenderId, selectedMessageId);
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }

      sendersPanel.removeAll();
      messagesPanel.removeAll();
      selectedMessagePanel.removeAll();
      lastMessagePanel.removeAll();
      
      sendersPanel.add(new JLabel("Senders"));
      sendersPanel.add( sendersTable() );
      
      messagesPanel.add(new JLabel("Messages"));
      messagesPanel.add( messagesTable() );
      
      selectedMessagePanel.add(new JLabel("Selected message"));
      if (model.getSelectedMessage() != null)
         selectedMessagePanel.add( messageTree(model.getSelectedMessage()) );
      
      lastMessagePanel.add(new JLabel("Last message"));
      if (model.getLastMessage() != null)
         lastMessagePanel.add( messageTree(model.getLastMessage()) );

      window.revalidate();
      window.repaint();
   }

   private JScrollPane sendersTable()
   {
      SenderInfoClient[] senders = model.getSenders();
      Object[][] content = new Object[senders.length][3];

      int selectedRow = -1;
      
      int i = 0;
      for (SenderInfoClient sender : senders)
      {
         int id = sender.getSenderId();

         if (selectedSenderId == id)
            selectedRow = i;
         
         content[i][0] = id;
         content[i][1] = sender.getSenderClass();
         content[i][2] = sender.getSenderName();

         i++;
      }
      
      String[] titles = {"id", "class", "name"};

      JTable table = new JTable(content, titles);
      table.setDefaultEditor(Object.class, null);

      table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event)
            {
               int selectedSenderId_ = (Integer) table.getValueAt(table.getSelectedRow(), 0);
               if (selectedSenderId_ != selectedSenderId)
               {
                  selectedSenderId  = selectedSenderId_;
                  selectedMessageId = -1;
                  refresh();
               }
            }
         });
      
      if (selectedRow >= 0)
         table.setRowSelectionInterval(selectedRow, selectedRow);
      
      JScrollPane result = new JScrollPane(table);
      
      if (sendersScroll != null)
         result.getViewport().setViewPosition( sendersScroll.getViewport().getViewPosition() );
      sendersScroll = result;
      
      return result;
   }

   private JScrollPane messagesTable()
   {
      ArrayList<MessageClient> messages = model.getMessages();
      Object[][] content = new Object[messages.size()][2];

      int selectedRow = -1;
      
      int i = 0;
      for (MessageClient msg : messages)
      {
         int id = msg.getId();

         if (selectedMessageId == id)
            selectedRow = i;
         
         content[i][0] = id;
         content[i][1] = msg.getDate();
         
         i++;
      }
      
      String[] titles = {"id", "date"};

      JTable table = new JTable(content, titles);
      table.setDefaultEditor(Object.class, null);

      table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event)
            {
               int selectedMessageId_ = (Integer) table.getValueAt(table.getSelectedRow(), 0);
               if (selectedMessageId_ != selectedMessageId)
               {
                  selectedMessageId = selectedMessageId_;
                  refresh();
               }
            }
         });

      if (selectedRow >= 0)
         table.setRowSelectionInterval(selectedRow, selectedRow);

      JScrollPane result = new JScrollPane(table);

      if (messagesScroll != null)
         result.getViewport().setViewPosition( messagesScroll.getViewport().getViewPosition() );
      messagesScroll = result;
      
      return result;
   }
   
   private JScrollPane messageTree(MessageClient msg)
   {
      DefaultMutableTreeNode root = jsonNode("contents", msg.getContent());

      JTree tree = new JTree(root) {
            protected void setExpandedState(TreePath path, boolean state)
            {}
         };

      tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
               tree.setSelectionPath(null);
            }
         });

      tree.setRootVisible(false);
      
      return new JScrollPane(tree);
   }
   
   private DefaultMutableTreeNode jsonNode(String key, JsonValue value)
   {
      if (value instanceof JsonArray)
         return jsonNode(key, (JsonArray) value);
      else if (value instanceof JsonObject)
         return jsonNode(key, (JsonObject) value);
      else if (value instanceof JsonNumber)
         return jsonNode(key, (JsonNumber) value);
      else // if (value instanceof JsonString)
         return jsonNode(key, (JsonString) value);
   }

   private DefaultMutableTreeNode jsonNode(String key, JsonArray arr)
   {
      DefaultMutableTreeNode result = new DefaultMutableTreeNode(key);

      int i = 0;
      for (JsonValue value : arr)
      {
         result.add( jsonNode(Integer.toString(i), value) );
         i++;
      }

      return result;
   }
   
   private DefaultMutableTreeNode jsonNode(String key, JsonObject obj)
   {
      DefaultMutableTreeNode result = new DefaultMutableTreeNode(key);

      for (Map.Entry<String, JsonValue> entry : obj.entrySet())
         result.add( jsonNode(entry.getKey(), entry.getValue()) );
      
      return result;
   }

   private DefaultMutableTreeNode jsonNode(String key, JsonNumber num)
   {
      return new DefaultMutableTreeNode(key + ": " + num);
   }

   private DefaultMutableTreeNode jsonNode(String key, JsonString str)
   {
      return new DefaultMutableTreeNode(key + ": " + str);
   }

   public void stop()
   {
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               try {
                  stop_();
               }
               catch (IOException e) {
                  throw new RuntimeException(e);
               }
            }});
   }

   private void stop_() throws IOException
   {
      if (timer != null)
         timer.stop();
      
      if (model != null)
         model.close();

      if (window != null)
         window.dispose();
   }

   static public void main(String[] args) throws IOException
   {
      new Monitor().start("localhost");
   }
}
