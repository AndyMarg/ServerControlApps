package su.vistar.servercontrol.entity;

import java.util.LinkedList;
import java.util.List;

public class ConnectionsEntity {

    private List<ConnectionEntity> connections;

    public List<ConnectionEntity> getConnections() {
        return connections;
    }

    public void setConnections(List<ConnectionEntity> connections) {
        this.connections = connections;
    }

    public void addConnection(ConnectionEntity connection) {
        if (this.connections == null) {
            this.connections = new LinkedList<>();
        }
        this.connections.add(connection);
    }
    
        @Override
    public String toString() {
        return "Connections [connections=" + connections + "]";
    }
}
