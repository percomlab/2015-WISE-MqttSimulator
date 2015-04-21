/*******************************
 * Author: Wei-Chen Chang 
 * Date: 4/21/2015
 * Current version: 1.0
 * Last modified: 4/21/2015
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
	static MqttAsyncClient MQTTAsyncClient;
	public ButtonListener() {

	}

	public static class btnSendListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Get message want to publish
			String message = MQTTSimulator.jtfSendMessage.getText();
			// Get topic and server name
			String strTopic= (String) MQTTSimulator.jcbDestinationName.getSelectedItem();
			String strServerUri= (String) MQTTSimulator.jcbServerUri.getSelectedItem();
			// Print message on TextArea
			MQTTSimulator.jtaMessageSent.append(message + "\n");

			try {
				// Connect to MQTT Server
	            MqttClient MQTTClient = new MqttClient(strServerUri, "MQTTSimulator_Pub", new MemoryPersistence());
	            MqttConnectOptions connOpts = new MqttConnectOptions();
	            connOpts.setCleanSession(true);
	            MQTTClient.connect(connOpts);
	            MqttMessage msg = new MqttMessage(message.getBytes());
	            // Set QOS
	            msg.setQos(0);
	            
	            // Publishes a message to a topic on the server
	            MQTTClient.publish(strTopic, msg);
	            
	            // Close the connection
	            MQTTClient.disconnect();
			} catch (Exception e1) {
			}
		}
	}

	public static class btnMessageSentClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Clean SendMessage TextArea
			MQTTSimulator.jtaMessageSent.setText(null);
		}
	}

	public static class btnMessageReceivedClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Clean ReceiveMessage TextArea
			MQTTSimulator.jtaMessageReceived.setText(null);
		}
	}

	public static class btnConnectListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String strTopic;
			String strServerUri;

			strTopic = (String) MQTTSimulator.jcbListenTopic.getSelectedItem();
			strServerUri = (String) MQTTSimulator.jcbMQTTUri.getSelectedItem();
			MemoryPersistence persistence = new MemoryPersistence();
			try {
				// Connect to MQTT Server
				MQTTAsyncClient = new MqttAsyncClient(strServerUri, "MQTTSimulator_Sub", persistence);
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(true);
				MQTTAsyncClient.setCallback(new MQTTListener());
				IMqttToken conToken = MQTTAsyncClient.connect(connOpts);
				conToken.waitForCompletion();

				// Subscribe to a topic
				IMqttToken subToken = MQTTAsyncClient.subscribe(strTopic, 0, null, null);
				subToken.waitForCompletion();
			} catch (Exception e1) {
			}
			// Hide connect button and show disconnect button
			MQTTSimulator.btnConnect.setVisible(false);
			MQTTSimulator.btnDisconnect.setVisible(true);
			MQTTSimulator.jcbMQTTUri.setEnabled(false);
			MQTTSimulator.jcbListenTopic.setEnabled(false);
		}
	}

	public static class btnDisconnectListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				MQTTAsyncClient.disconnect();
			} catch (MqttException e1) {
			}
			// Show connect button and hide disconnect button
			MQTTSimulator.btnConnect.setVisible(true);
			MQTTSimulator.btnDisconnect.setVisible(false);
			MQTTSimulator.jcbMQTTUri.setEnabled(true);
			MQTTSimulator.jcbListenTopic.setEnabled(true);
		}
	}

	public void actionPerformed(ActionEvent e) {

	}
}
