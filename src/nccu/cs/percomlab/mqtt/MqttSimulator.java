/*******************************
 * Author: Wei-Chen Chang 
 * Date: 4/21/2015
 * Current version: 1.3
 * Last modified: 08/03/2016
 */

package nccu.cs.percomlab.mqtt;

import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSimulator extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static UUID sub_uuid, pub_uuid;
	
	// JLabel declaration
	private JLabel jlbMessageSent;
	private JLabel jlbDestinationName;
	private JLabel jlbServerUri;

	// private JLabel jlbDefaultUri;
	private JLabel jlbMessageReceived;
	private JLabel jlbMqttUri;
	private JLabel jlbListenTopic;

	// Button declaration
	private Button btnMessageSentClear;
	private Button btnSend;
	private Button btnMessageReceivedClear;
	public static Button btnConnect;
	public static Button btnDisconnect;

	// JScrollPane declaration
	private JScrollPane jspMessageSent;
	private JScrollPane jspMessageReceived;

	// JTextArea declaration
	public static JTextArea jtaMessageSent;
	public static JTextArea jtaMessageReceived;

	// JTextField declaration
	public static JTextField jtfSendMessage;

	// JComboBox declaration
	public static JComboBox<String> jcbDestinationName;
	public static JComboBox<String> jcbServerUri;
	public static JComboBox<String> jcbMqttUri;
	public static JComboBox<String> jcbListenTopic;

	public static JScrollBar vertical;
	String strTopicName[] = { "testtopic", "stage/dancer/sensor/position",
			"stage/dancer/sensor/rotation", "stage/dancer/sensor/rotation_raw",
			"stage/command", "stage/context" };
	String strServerUri[] = { "tcp://wearable.nccu.edu.tw:1883" };

	private static MqttAsyncClient mqttAsyncClient;
	
	public MqttSimulator() {
		sub_uuid = UUID.randomUUID();
		pub_uuid = UUID.randomUUID();
		init();
	}

	private void init() {
		initComponents();
		initEventListeners();
		initJLabel();
		initButton();
		initTextArea();
		initTextField();
		initComboBox();
	}

	private void initComponents() {
		setTitle("MQTTSimulator");
		setSize(500, 600);
		setLayout(null);
		setLocation(350, 250);
		setResizable(false);
		setVisible(true);
	}

	private void initEventListeners() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initJLabel() {
		initMessageSentLabel();
		initDestinationNameLabel();
		initServerUriLabel();
		initMessageReceivedLabel();
		initMqttUriLabel();
		initListenTopicLabel();
	}

	private void initButton() {
		initMessageSentClearButton();
		initSendButton();
		initMessageReceivedClearButton();
		initConnectButton();
		initDisconnectButton();
	}

	private void initTextArea() {
		initMessageSentTextArea();
		initMessageSentScrollPane();
		initMessageReceivedTextArea();
		initMessageReceivedScrollPane();
	}

	private void initTextField() {
		initSendMessageTextField();
	}

	private void initComboBox() {
		initDestinationNameComboBox();
		initServerUriComboBox();
		initMqttUriComboBox();
		initListenTopicComboBox();
	}

	private void initListenTopicLabel() {
		jlbListenTopic = new JLabel("Listen Topic:");
		jlbListenTopic.setFont(new java.awt.Font("Dialog", 0, 13));
		jlbListenTopic.setBounds(10, 535, 150, 25);
		add(jlbListenTopic);
	}

	private void initMqttUriLabel() {
		jlbMqttUri = new JLabel("Server URL:");
		jlbMqttUri.setFont(new java.awt.Font("Dialog", 0, 13));
		jlbMqttUri.setBounds(10, 500, 150, 25);
		add(jlbMqttUri);
	}

	private void initMessageReceivedLabel() {
		jlbMessageReceived = new JLabel("接收訊息");
		jlbMessageReceived.setFont(new java.awt.Font("Dialog", 0, 13));
		jlbMessageReceived.setBounds(10, 310, 70, 25);
		add(jlbMessageReceived);
	}

	private void initServerUriLabel() {
		jlbServerUri = new JLabel("Server URL:");
		jlbServerUri.setFont(new java.awt.Font("Dialog", 0, 13));
		jlbServerUri.setBounds(10, 260, 150, 25);
		add(jlbServerUri);
	}

	private void initDestinationNameLabel() {
		jlbDestinationName = new JLabel("Topic Name: ");
		jlbDestinationName.setFont(new java.awt.Font("Dialog", 0, 13));
		jlbDestinationName.setBounds(10, 225, 150, 25);
		add(jlbDestinationName);
	}

	private void initMessageSentLabel() {
		jlbMessageSent = new JLabel("傳送訊息");
		jlbMessageSent.setFont(new java.awt.Font("Dialog", 0, 13));
		jlbMessageSent.setBounds(10, 5, 70, 25);
		add(jlbMessageSent);
	}

	private void initDisconnectButton() {
		btnDisconnect = new Button("Disconnect");
		btnDisconnect.setFont(new java.awt.Font("Dialog", 0, 13));
		btnDisconnect.setBounds(410, 500, 75, 25);
		btnDisconnect
				.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						closeConnection();
						setButton();
					}

					private void closeConnection() {
						try {
							mqttAsyncClient.disconnect();
						} catch (MqttException e1) {
						}
					}

					private void setButton() {
						showConnectButton();
						hideDisconnectButton();
					}

					private void showConnectButton() {
						MqttSimulator.btnConnect.setVisible(true);
						enableEdit();
					}

					private void enableEdit() {
						MqttSimulator.jcbMqttUri.setEnabled(true);
						MqttSimulator.jcbListenTopic.setEnabled(true);
					}

					private void hideDisconnectButton() {
						MqttSimulator.btnDisconnect.setVisible(false);
					}
				});
		btnDisconnect.setVisible(false);
		add(btnDisconnect);
	}

	private void initConnectButton() {
		btnConnect = new Button("Connect");
		btnConnect.setFont(new java.awt.Font("Dialog", 0, 13));
		btnConnect.setBounds(410, 500, 75, 25);
		btnConnect.addActionListener(new ActionListener(){		
			public void actionPerformed(ActionEvent e) {
				String listenTopic = getTopic();
				String listenServerUri = getServerUri();

				createConnection(listenServerUri);
				subscribeTopic(listenTopic);

				setButton();
				MqttSimulator.jtaMessageReceived.setText(null);
			}

			private String getTopic() {
				return (String) MqttSimulator.jcbListenTopic.getSelectedItem();
			}

			private String getServerUri() {
				return (String) MqttSimulator.jcbMqttUri.getSelectedItem();
			}

			private void createConnection(String listenServerUri) {
				try {
					mqttAsyncClient = new MqttAsyncClient(listenServerUri,
							sub_uuid.toString(), new MemoryPersistence());
					MqttConnectOptions connOpts = new MqttConnectOptions();
					connOpts.setCleanSession(true);
					mqttAsyncClient.setCallback(new MqttCallback(){
						@Override
					    public void connectionLost(Throwable arg0)
					    {
					        // Do nothing
					    }

					    @Override
					    public void deliveryComplete(IMqttDeliveryToken arg0)
					    {
					        // Do nothing
					    }
					    
					    // Called when delivery for a message has been completed
					    @Override
					    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
					    {
					    	appendToTextArea(mqttMessage);
					    }

						private void appendToTextArea(MqttMessage mqttMessage) {
							MqttSimulator.vertical.setValue( MqttSimulator.vertical.getMaximum() );
					    	MqttSimulator.jtaMessageReceived.append(mqttMessage.toString() + "\n");
						}
					});
					IMqttToken conToken = mqttAsyncClient.connect(connOpts);
					conToken.waitForCompletion();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void subscribeTopic(String listenTopic) {
				IMqttToken subToken;
				try {
					subToken = mqttAsyncClient
							.subscribe(listenTopic, 0, null, null);
					subToken.waitForCompletion();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}

			private void setButton() {
				hideConnectButton();
				showDisconnectButton();
			}

			private void hideConnectButton() {
				MqttSimulator.btnConnect.setVisible(false);
			}

			private void showDisconnectButton() {
				MqttSimulator.btnDisconnect.setVisible(true);
				disableEdit();
			}

			private void disableEdit() {
				MqttSimulator.jcbMqttUri.setEnabled(false);
				MqttSimulator.jcbListenTopic.setEnabled(false);
			}
		});
		add(btnConnect);
	}

	private void initMessageReceivedClearButton() {
		btnMessageReceivedClear = new Button("Clear");
		btnMessageReceivedClear.setFont(new java.awt.Font("Dialog", 0, 13));
		btnMessageReceivedClear.setBounds(420, 310, 65, 25);
		btnMessageReceivedClear
				.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						cleanTextArea();
					}

					private void cleanTextArea() {
						MqttSimulator.jtaMessageReceived.setText(null);
					}
				});
		add(btnMessageReceivedClear);
	}

	private void initSendButton() {
		btnSend = new Button("Send");
		btnSend.setFont(new java.awt.Font("Dialog", 0, 13));
		btnSend.setBounds(420, 190, 65, 25);
		btnSend.addActionListener(new ActionListener(){
			MqttClient MQTTClient;

			public void actionPerformed(ActionEvent e) {
				String messageSent = getSendMessage();
				showSendMessage(messageSent);
				String sendTopic = getTopic();
				String sendServerUri = getServerUri();

				createConnection(sendServerUri);
				publishMessage(sendTopic, messageSent);
				closeConnection();
			}

			private String getSendMessage() {
				return MqttSimulator.jtfSendMessage.getText();
			}

			private void showSendMessage(String messageSent) {
				MqttSimulator.jtaMessageSent.append(messageSent + "\n");
			}

			private String getTopic() {
				return (String) MqttSimulator.jcbDestinationName.getSelectedItem();
			}

			private String getServerUri() {
				return (String) MqttSimulator.jcbServerUri.getSelectedItem();
			}

			private void createConnection(String sendServerUri) {
				try {
					MQTTClient = new MqttClient(sendServerUri, pub_uuid.toString(),
							new MemoryPersistence());
					MqttConnectOptions connOpts = new MqttConnectOptions();
					connOpts.setCleanSession(true);
					MQTTClient.connect(connOpts);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			private void publishMessage(String sendTopic, String messageSent) {
				MqttMessage msg = new MqttMessage(messageSent.getBytes());
				msg.setQos(0);

				try {
					MQTTClient.publish(sendTopic, msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void closeConnection() {
				try {
					MQTTClient.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		add(btnSend);
	}

	private void initMessageSentClearButton() {
		btnMessageSentClear = new Button("Clear");
		btnMessageSentClear.setFont(new java.awt.Font("Dialog", 0, 13));
		btnMessageSentClear.setBounds(420, 5, 65, 25);
		btnMessageSentClear
				.addActionListener(new ActionListener() {				
					public void actionPerformed(ActionEvent e) {
						cleanTextArea();
					}

					private void cleanTextArea() {
						MqttSimulator.jtaMessageSent.setText(null);
					}
				});
		add(btnMessageSentClear);
	}

	private void initMessageReceivedScrollPane() {
		jspMessageReceived = new JScrollPane(jtaMessageReceived);
		jspMessageReceived.setBounds(10, 340, 475, 150);
		vertical = jspMessageReceived.getVerticalScrollBar();
		add(jspMessageReceived);
	}

	private void initMessageReceivedTextArea() {
		jtaMessageReceived = new JTextArea();
		jtaMessageReceived.setEditable(false);
		jtaMessageReceived.setFocusable(false);
		jtaMessageReceived.setLineWrap(true);
		jtaMessageReceived.setWrapStyleWord(true);
	}

	private void initMessageSentScrollPane() {
		jspMessageSent = new JScrollPane(jtaMessageSent);
		jspMessageSent.setBounds(10, 35, 475, 150);
		add(jspMessageSent);
	}

	private void initMessageSentTextArea() {
		jtaMessageSent = new JTextArea();
		jtaMessageSent.setEditable(false);
		jtaMessageSent.setFocusable(false);
		jtaMessageSent.setLineWrap(true);
		jtaMessageSent.setWrapStyleWord(true);
	}

	private void initSendMessageTextField() {
		jtfSendMessage = new JTextField();
		jtfSendMessage.setBounds(10, 190, 400, 25);
		add(jtfSendMessage);
	}

	private void initListenTopicComboBox() {
		jcbListenTopic = new JComboBox<>(strTopicName);
		jcbListenTopic.setBounds(90, 535, 310, 25);
		jcbListenTopic.setEditable(true);
		jcbListenTopic.setFont(new java.awt.Font("Dialog", 0, 13));
		jcbListenTopic.setBackground(Color.white);
		add(jcbListenTopic);
	}

	private void initMqttUriComboBox() {
		jcbMqttUri = new JComboBox<>(strServerUri);
		jcbMqttUri.setBounds(90, 500, 310, 25);
		jcbMqttUri.setEditable(true);
		jcbMqttUri.setFont(new java.awt.Font("Dialog", 0, 13));
		jcbMqttUri.setBackground(Color.white);
		add(jcbMqttUri);
	}

	private void initServerUriComboBox() {
		jcbServerUri = new JComboBox<>(strServerUri);
		jcbServerUri.setBounds(130, 260, 355, 25);
		jcbServerUri.setEditable(true);
		jcbServerUri.setFont(new java.awt.Font("Dialog", 0, 13));
		jcbServerUri.setBackground(Color.white);
		add(jcbServerUri);
	}

	private void initDestinationNameComboBox() {
		jcbDestinationName = new JComboBox<>(strTopicName);
		jcbDestinationName.setBounds(130, 225, 355, 25);
		jcbDestinationName.setEditable(true);
		jcbDestinationName.setFont(new java.awt.Font("Dialog", 0, 13));
		jcbDestinationName.setBackground(Color.white);
		add(jcbDestinationName);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			new MqttSimulator().setVisible(true);
		});
	}
}
