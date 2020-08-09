# MouseFighter
Java based shoot 'em up game
This is a Shmup I created entirely in Java. Graphics are fairly primitive, generated using only the Java library's shape methods drawn onto JPanels. There is only one enemy
however it has different attack patterns as its hit points deplete. 

Notable features include hit detection, user input using Java's EventListener libraries, fairly extensive use of trigonometry to calculate the distance and angle each projectile
moves each frame, and a score tracking system though it is implemented locally on the machine for now. 

Looking back there are definitely some improvements to be made, for example the update method could be optimized more to remove bloat as it runs on every frame the game draws. Even
small optimizations to the code's runtime in the update method, or deferring other methods elsewhere, would likely have the largest effect on performance.

Overall I am proud of this project since it is my first complete game I've produced, and it demonstrates a large variety of different tools and mathematical concepts
available in Java working in tandem to create a complete application.
