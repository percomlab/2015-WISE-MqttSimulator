package nccu.cs.percomlab.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttListener implements MqttCallback
{
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

}