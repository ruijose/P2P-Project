ssh -f istple_seprs4@onelab3.warsaw.rd.tp.pl  'cd my_project; ant -Darg0=3000 runTestAcceptBid'
ssh -f istple_seprs4@ple2.ipv6.lip6.fr 'cd my_project; ant -Darg0=3001 runIdle'
ssh -f istple_seprs4@ple1.dmcs.p.lodz.pl 'cd my_project; ant -Darg0=3002 runIdle'
ssh -f istple_seprs4@planetlab1.cs.uoi.gr 'cd my_project; ant -Darg0=3003 runIdle'
ssh -f istple_seprs4@iraplab2.iralab.uni-karlsruhe.de 'cd my_project; ant -Darg0=3004 runIdle'
ssh -f istple_seprs4@planetlab1.di.unito.it 'cd my_project; ant -Darg0=3005 runIdle'
ssh -f istple_seprs4@mars.planetlab.haw-hamburg.de 'cd my_project; ant -Darg0=3006 runIdle'
ssh -f istple_seprs4@planetlab2.jcp-consult.net 'cd my_project; ant -Darg0=3007 runIdle'
ssh -f istple_seprs4@ple2.ipv6.lip6.fr 'cd my_project; ant -Darg0=3008 runIdle'
ssh -f istple_seprs4@ple1.dmcs.p.lodz.pl 'cd my_project; ant -Darg0=3009 runIdle'
ssh -f istple_seprs4@planetlab1.cs.uoi.gr 'cd my_project; ant -Darg0=3010 runIdle'
ssh -f istple_seprs4@iraplab2.iralab.uni-karlsruhe.de 'cd my_project; ant -Darg0=3011 runIdle'
ssh -f istple_seprs4@planetlab1.di.unito.it 'cd my_project; ant -Darg0=3012 runIdle'
sleep 10s
ssh istple_seprs4@prata.mimuw.edu.pl  'cd my_project; ant -Darg0=30013 runTestAddBid'
