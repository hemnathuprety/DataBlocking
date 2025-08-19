package eu.faircode.netguard;

// Anjul Khanal

public class Forward {
    public int protocol;
    public int dport;
    public String raddr;
    public int rport;
    public int ruid;

    @Override
    public String toString() {
        return "protocol=" + protocol + " port " + dport + " to " + raddr + "/" + rport + " uid " + ruid;
    }
}
