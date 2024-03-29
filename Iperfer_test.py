#!/usr/bin/env python
"""

Iperfer test for CS640, Spring 2017, Assignment 1
"""

from mininet.cli import CLI
from mininet.net import Mininet
from mininet.link import TCLink
from mininet.topo import Topo
from mininet.log import setLogLevel

class AssignmentNetworks(Topo):
    def __init__(self, **opts):
        Topo.__init__(self, **opts)
        h1 = self.addHost('h1')
        h2 = self.addHost('h2')
        h3 = self.addHost('h3')
        h4 = self.addHost('h4')
        h5 = self.addHost('h5')
        h6 = self.addHost('h6')
        h7 = self.addHost('h7')
        h8 = self.addHost('h8')
        h9 = self.addHost('h9')
        h10 = self.addHost('h10')
        s1 = self.addSwitch('s1')
        s2 = self.addSwitch('s2')
        s3 = self.addSwitch('s3')
        s4 = self.addSwitch('s4')
        s5 = self.addSwitch('s5')
        s6 = self.addSwitch('s6')
        self.addLink(h1, s1)
        self.addLink(h7, s1)
        self.addLink(h8, s1)
        self.addLink(h2, s2)
        self.addLink(h3, s3)
        self.addLink(h4, s4)
        self.addLink(h9, s4)
        self.addLink(h10, s4)
        self.addLink(h5, s5)
        self.addLink(h6, s6)
        self.addLink(s1, s2, bw=20, delay='40ms')
        self.addLink(s2, s3, bw=40, delay='10ms')
        self.addLink(s3, s4, bw=30, delay='30ms')
        self.addLink(s2, s5, bw=25, delay='5ms')
        self.addLink(s3, s6, bw=25, delay='5ms')
 
if __name__ == '__main__':
    setLogLevel( 'info' )

    # Create data network
    topo = AssignmentNetworks()
    net = Mininet(topo=topo, link=TCLink,autoSetMacs=True,
           autoStaticArp=True)

    # Run network
    net.start()
    h1 = net.getNodeByName("h1")
    h4 = net.getNodeByName("h4")
    h5 = net.getNodeByName("h5")
    h6 = net.getNodeByName("h6")
    h7 = net.getNodeByName("h7")
    h8 = net.getNodeByName("h8")
    h9 = net.getNodeByName("h9")
    h10 = net.getNodeByName("h10")

    # note that for this to work the
    # the Iperfer should be in the working directory
    """
    print "running Iperfer Servers on h1 and h7"
    h1.sendCmd("java Iperfer -s -p 2222")
    h7.sendCmd("java Iperfer -s -p 2222")
    h8.sendCmd("java Iperfer -s -p 2222")
    """
    """
    h1.sendCmd("ping -c 20 10.0.0.4")
    h7.sendCmd("ping -c 20 10.0.0.10")
    h4.sendCmd("ping -c 20 10.0.0.1")
    h10.sendCmd("ping -c 20 10.0.0.7")
    h8.sendCmd("ping -c 20 10.0.0.9")
    h9.sendCmd("ping -c 20 10.0.0.8")
    """
    """    
    print "running Iperfer clients on h4 and h10"
    h4.sendCmd("java Iperfer -c -h 10.0.0.1 -p 2222 -t 60")
    h10.sendCmd("java Iperfer -c -h 10.0.0.7 -p 2222 -t 60")
    h9.sendCmd("java Iperfer -c -h 10.0.0.8 -p 2222 -t 60")
    print "waiting for Iperfer output from hosts"
    """

    # Q4 servers
    h4.sendCmd("java Iperfer -s -p 2222")
    h6.sendCmd("java Iperfer -s -p 2222")

    # Q4 clients
    h1.sendCmd("java Iperfer -c -h 10.0.0.4 -p 2222 -t 60")
    h5.sendCmd("java Iperfer -c -h 10.0.0.6 -p 2222 -t 60")
    #h1.sendCmd("ping -c 20 10.0.0.4")
    #h5.sendCmd("ping -c 20 10.0.0.6")


    res1 = h1.waitOutput()
    res4 = h4.waitOutput()
    res5 = h5.waitOutput()
    res6 = h6.waitOutput()
    
    """
    res7 = h7.waitOutput()
    res8 = h8.waitOutput()
    res9 = h9.waitOutput()
    res10 = h10.waitOutput()
    """

    print "h1 result"
    print res1

    print "h4 result"
    print res4

    print "h5 result"
    print res5

    print "h6 result"
    print res6

    """
    print "h7 result"
    print res7
 
    print "h8 result"
    print res8

    print "h9 result"
    print res9
    
    print "h10 result"
    print res10
    """

    #CLI( net )
    net.stop()
