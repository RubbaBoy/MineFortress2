


About The Game
-------------
Mine Fortress 2 is a remake of the popular game [Team Fortress 2](http://www.teamfortress.com/ "Team Fortress 2") (TF2). All of the default guns from the original game are remade with the same specifications from TF2, along with classes, and a never before seen seamless GUI for clips, remaining ammo, health, and metal. Mine Fortress 2 has its own custom made map to go with the PvP gamemode, and soon the [Control Points](https://wiki.teamfortress.com/wiki/Control_Point_%28game_mode%29) gamemode.

How To Play
-------------
**To get the game working, you need to make you sure you followed the correct instillation steps found in the next section of this page.**

When you first get on your server, make sure to enable the MF2 vX.X Resourcepack. To start a game, type in chat **/mf2 start**. Everyone will be teleported into an area to choose a team. Right click the "Random" door for the system to choose a team at random for you, "Red" to choose the red team, and "Blue" to choose the blue team. Click the door of the team you want, and you will be teleported to that team's base. Next, choose your class on the GUI inventory that pops up on the screen. After choosing your class, you will be shown A loadout GUI. All of the loadouts are saved in a database, so you will see your last created loadout on screen. If you want to continue with your current loadout, click ESC. If you want to change any of the weaponary/PDA's, click the item and then click the one you want to replace it with.

To shoot guns, hold right click if it is an automatic gun (Ex. Machine gun, Submachine gun). Ammo can be gained by standing in front of your team's dispenser (Located within a few blocks of your spawn), or ammo packs spawning in certain locations in the map. Health can also be gained by the dispenser as well, and health packs, usually spawning next to ammo packs.

Installation
-------------
The first step in installing Mine Fortress 2 is to download the necessary files from the [uddernetworks website](http://www.uddernetworks.com/download). Unzip the file to see the following files/folders:s
>- MF2
>- MF2 vX.X Map
>- libs.zip
>- MF2 vX.X Resourcepack.zip
>- MineFortress2_vX.X.jar

*(**X.X** is replaced with the version you download from the website.)*
Put **MF2 vX.X Map** in the server's worlds folder. Go to the server's plugins folder and put **MineFortress2_vX.X.jar** into it. Extract **libs.zip** into the plugins folder as well, making sure the jar files found in it are found directly in the **libs** folder in the server's plugin directory. Lastly, put the **MF2** folder in your server's plugin folder as well. You are now free to modify the config file or guns.xlsx.

###config.yml
The following are the lines from the **config.yml** file, and what they mean.

>anonymous-error-messages: true

**true/false** value, when **true**, the error GUI does not ask you for your email. If next line is **true**, this is overwritten.
>silent-error-reports: true

**true/false** value, when **true**, no GUI pops up to ask you for information when an error happens, anonymous information only involving hosting server's operating system architecture, name, java version, minecraft server version, other plugins on the server, and the error produced by the server itself.
>world: mf2_game

**string** value, the name of the world the team chooser is in
>- random-door-X: -286
>- random-door-Y: 70
>- random-door-Z: -148

**integer** value, the coordinates of the "random" team choosing door (Bottom block of the door)
>- blue-door-X: -284
>- blue-door-Y: 70
>- blue-door-Z: -148

**integer** value, the coordinates of the "blue" team choosing door (Bottom block of the door)
>- red-door-X: -282
>- red-door-Y: 70
>- red-door-Z: -148

**integer** value, the coordinates of the "red" team choosing door (Bottom block of the door)
>- team-choose-spawn-X: -283
>- team-choose-spawn-Y: 70
>- team-choose-spawn-Z: -144

**integer** value, the coordinates of where the players spawn to pick their teams
>- blue-sign-X: -284
>- blue-sign-Y: 72
>- blue-sign-Z: -147

**integer** value, the coordinates of the sign above the blue door
>- red-sign-X: -282
>- red-sign-Y: 72
>- red-sign-Z: -147

**integer** value, the coordinates of the sign above the red door
>playworlds:

**list** of the worlds in use for the game (**mf2_game** only one being used now)
>mf2_game:

**world name** value, the name of the world for the world being used next in the configuration section
>- spectate-X: -59
>- spectate-Y: 80
>- spectate-Z: 40

**integer** value, the coordinates you spawn when you die for a few seconds, and where you spawn when the spectate gamemode is added
>red

**no value** this is a required value for each world, everything after this will be for the red team
>- spawn1-X: -116
>- spawn1-Y: 65
>- spawn1-Z: 5
>- spawn2-X: -112
>- spawn2-Y: 65
>- spawn2-Z: 7

**integer** value, the two sets of coordinates which players will spawn in-between randomly
>- refill-door-X: -115
>- refill-door-Y: 65
>- refill-door-Z: 3

**integer** value, the bottom block of either of the team's dispenser doors
>barriers:

**list** of the barriers that the opposite team can not cross into spawn
>- barrier-1:
>- 1-X: -101
>- 1-Y: 67
>- 1-Z: 6
>- 2-X: -191
>- 2-Y: 65
>- 2-Z: 4

**integer** value, the two corner coordinates of the invisible barriers that players on the opposite team cannot cross. The number **1** in **barrier-1** can be added by one for each new barrier, and for that barrier alone (Starting with 1)
>blue:

**no value** a direct child of the world name as **red** is, contains all the same variables as the red team, but changed the values for the other team
>healths:

**list** of the health packs that spawn
>- health-0:
>- X: -24.5
>- Y: 66
>- Z: 47.5

**integer** value, the coordinates of where the health pack will spawn in the game world. Like the barriers, the **0** in **health-0** goes up in intervals of 1 with each new health pack location, but starting at 0
>ammos:

**list** of the ammo packs that spawn, a direct child of the world name again
>- ammo-0:
>- X: -24.5
>- Y: 66
>- Z: 49.5

**integer** value, the coordinates of where the ammo pack will spawn in the game world. Like the health packs, the **0** in **ammo-0** goes up in intervals of 1 with each new ammo pack location

###guns.xlsx
The following section is on the guns.xlsx sheet columns, that can be edited on [Google Sheets](http://sheets.google.com/), or [Microsoft Excel](https://office.live.com/start/Excel.aspx).

>Type

**enumerator** value, which can be either **PRIMARY**, **SECONDARY**, **MELEE**, or **PDA**
>Name

**string** value, the name of the gun, accepts [Minecraft formatting codes](http://minecraft.gamepedia.com/Formatting_codes) with the symbol **§**. Can **NOT** be a duplicate of another gun's name, or else errors will occur
>Lore

**string** value, the description of the gun when you hover over the item (Can be null)
>Item

**enumerator** value, defines the type of item the gun is. Values can be found [here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html)
>Sound

**enumerator** value, defined the sound played when you use the weapon, can be **NULL** if you don't want a sound
>Power

**double** value, from 0 to 10, defines how fast the bullets move, and how far they move
>Damage

**double** value, the amount of damage the gun delivers per hit (Remember: these values are scaled up to work with the health in the game, not player health)
>Kill-zone radius

**double** value, the radius of blocks it kills other players when the bullet hits a block or player
>Scopeable

**true/false** value, defines if you can right click to scope the gun
>Night vision scope

**true/false** value, defines if the scope has the night vision effect only when used
>Max Clip

**integer** value, the maximum amount of bullets you can have in your gun's clip before needing a reload
>Max Ammo

**integer** value, the maximum amount of ammo you can have before you need to replenish it
>Cooldown

**integer** balue, the shortest time possible inbetween each shot (Time in milliseconds)
>Reload Cooldown

**integer** value, the time it takes to reload the gun (Time in milliseconds)
>Tracers

**true/false** value, defines if the bullets leave tracers (Primarily for snipers)
>Accuracy

**integer** value, from 0 to 10, defining how accurate the gun is, 0 being least accurate, 10 being dead accurate
>Shotgun

**true/false** value, defines if the gun is a shotgun (Shoots multiple bullets)
>Shotgun Bullet

**integer** value, if **shotgun** is enabled, the gun shoots this many bullets
>Class

**enumerator** value, the class the gun appears in, in the loadout. Valid classes are **SCOUT**, **SOLDIER**, **PYRO**, **DEMOMAN**, **HEAVY**, **ENGINEER**, **MEDIC**, **SNIPER**, and **SPY**
>Class Default

**true/false** value, if **true** the gun is automatically in the loadout of the class specified
>Custom Identifier

**custom identifier type**, used to give guns hard-coded effects, like healing or flamethrowing powers. Valid identifiers at the moment are: **medic.medi**, **engineer.wrench**, **engineer.construction**, **engineer.destruction**, **spy.watch**, **demoman.grenade_launcher**, **demoman.stickybomb**, **pyro.flamethrower**, and **soldier.rocket_launcher**
>Show GUI

**true/false** value, specifies if the ammo/clip GUI should be shown while holding the item, used for melee weapons, or PDAs
>Left Click

**true/false** value, specified if the gun needs to be left clicked to activate. **false** should be for automatic/fast paced guns, but can be used differently
