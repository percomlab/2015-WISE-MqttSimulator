/*******************************
 * Author: Wei-Chen Chang 
 * Date: 4/21/2015
 * Current version: 1.2
 * Last modified: 05/27/2015
 */

package nccu.cs.percomlab.mqtt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ButtonListener implements ActionListener {
	private static MqttAsyncClient mqttAsyncClient;

	public ButtonListener() {

	}

	public static class btnSendListener implements ActionListener {
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
				MQTTClient = new MqttClient(sendServerUri, "MQTTSimulator_Pub",
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
	}

	public static class btnMessageSentClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			cleanTextArea();
		}

		private void cleanTextArea() {
			MqttSimulator.jtaMessageSent.setText(null);
		}
	}

	public static class btnMessageReceivedClearListener implements
			ActionListener {
		public void actionPerformed(ActionEvent e) {
			cleanTextArea();
		}

		private void cleanTextArea() {
			MqttSimulator.jtaMessageReceived.setText(null);
		}
	}

	public static class btnConnectListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String listenTopic = getTopic();
			String listenServerUri = getServerUri();

			createConnection(listenServerUri);
			subscribeTopic(listenTopic);

			setButton();
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
						"MQTTSimulator_Sub", new MemoryPersistence());
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(true);
				mqttAsyncClient.setCallback(new MqttListener());
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
	}

	public static class btnDisconnectListener implements ActionListener {
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
	}

	public void actionPerformed(ActionEvent e) {

	}
}
