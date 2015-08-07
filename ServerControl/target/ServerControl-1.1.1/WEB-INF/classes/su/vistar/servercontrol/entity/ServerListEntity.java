package su.vistar.servercontrol.entity;

import java.util.ArrayList;
import java.util.List;

public class ServerListEntity {

    public List<ServerEntity> servers;

    public List<ServerEntity> getServers() {
        return servers;
    }

    public void setServers(List<ServerEntity> servers) {
        this.servers = servers;
    }

    public void addServer(ServerEntity server) {
        if (this.servers == null) {
            this.servers = new ArrayList<>();
        }
        this.servers.add(server);
    }

    @Override
    public String toString() {
        return "ServerListEntity{" + "servers=" + servers + '}';
    }

}
