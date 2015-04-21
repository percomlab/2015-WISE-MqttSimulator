package nccu.cs.percomlab.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTListener implements MqttCallback
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
    	MQTTSimulator.vertical.setValue( MQTTSimulator.vertical.getMaximum() );
    	MQTTSimulator.jtaMessageReceived.append(mqttMessage.toString() + "\n");
    }

}