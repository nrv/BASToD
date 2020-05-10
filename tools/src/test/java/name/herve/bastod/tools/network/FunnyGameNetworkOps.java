package name.herve.bastod.tools.network;

import java.awt.Color;
import java.awt.geom.Point2D;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;

public class FunnyGameNetworkOps {
	public static class AddBallMessage {
		public Ball ball;
	}
	
	public static class SetUUIDMessage {
		public String uuid;
	}

	static class ClientConnection extends Connection {
		private String uuid;

		public ClientConnection(String uuid) {
			super();
			this.uuid = uuid;
		}

		public String getUuid() {
			return uuid;
		}
	}

	public final static int tcpPort = 54741;
	public final static int udpPort = 54742;

	public static void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(Color.class);
		kryo.register(float[].class);
		kryo.register(Point2D.Double.class);
		kryo.register(Ball.class);
		kryo.register(AddBallMessage.class);
		kryo.register(SetUUIDMessage.class);
	}
}
