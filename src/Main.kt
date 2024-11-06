/**
 * ------------------------------------------------------------------------
 * Ever-Changing Maze
 * Level 2 programming project
 *
 * by Brianna Conning-Newsham
 *
 * I'm making an ever-changing maze, the maze doesn't really change that's just the name of the land as the area keeps changing
 * ------------------------------------------------------------------------
 */


import com.formdev.flatlaf.FlatDarkLaf
import java.awt.Dimension
import java.awt.Font
import java.awt.Rectangle
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*


//=============================================================================================
class Location(val name: String, val description: String, val item: String? = null, val isEnd:Boolean = false) {
    var north: Int = -1
    var east: Int = -1
    var south: Int = -1
    var west: Int = -1

    fun link(direction: String, locationIndex: Int) {
        if (direction == "N") north = locationIndex
        if (direction == "E") east = locationIndex
        if (direction == "S") south = locationIndex
        if (direction == "W") west = locationIndex
    }
}

class Inventory {
    private val items = mutableListOf<String>()

    fun addItem(item: String) {
        if (item !in items) {
            items.add(item)
            println("You picked up: $item")
        } else {
            println("You already have: $item")
        }
    }

    fun countItems(): Int {
        return items.size
    }

    fun displayItems(): String {
        return if (items.isEmpty()) "Inventory: Empty" else "Inventory: ${items.joinToString(", ")}"
    }
}
/**
 * GUI class
 * Defines the UI and responds to events
 */
class GUI : JFrame(), ActionListener {

    val locations = mutableListOf<Location>()
    var currentLocationIndex: Int
    private val playerInventory = Inventory()


    // Setup some properties to hold the UI elements
    private lateinit var nameLabel: JLabel
    private lateinit var descLabel: JLabel
    private lateinit var inventoryLabel: JLabel

    private lateinit var northButton: JButton
    private lateinit var westButton: JButton
    private lateinit var southButton: JButton
    private lateinit var eastButton: JButton


    /**
     * Create, build and run the UI
     */
    init {
        setupData()
        setupWindow()
        buildUI()


        // Show the app, centred on screen
        setLocationRelativeTo(null)
        isVisible = true

        // Show first location
        currentLocationIndex = 0
        updateUI()

    }

    private fun setupData() {

        // The Path to the door
        locations.add(Location("Forest", "It is dark and gloomy. Push the buttons at the bottom left side of the screen."))           // 0
        locations.add(Location("Woods","The trees are large and lean in. N = North, E = East, S = South, and W = West."))   // 1
        locations.add(Location("Path","A well worn path lays in the middle of the woods. Where does it go? You're goal is to get out, to get to the portal that can take you home."))             // 2
        locations.add(Location("Up the path","Nothing here. Some paths you can't go back on, so chose your path carefully."))    // 3
        locations.add(Location("More Path", "The path goes on. Does it stop? Make sure you find the key hidden in the maze, or you won't be able to get out.")) // 4
        locations.add(Location("A split","The path splits. Which way should I go?")) // 5
        locations.add(Location("A dark room", "A dark room? Why is this here? And where did it come from? What's this? A gem?", "Saphire")) // 6
        locations.add(Location("A hill", "A hill. Great....")) // 7
        locations.add(Location("On top the hill", "That- Huff- was a very- huff- big hill! Hey what's over there?")) // 8
        locations.add(Location("Looking down the hill", "Is that a stream?")) // 9
        locations.add(Location("Steam", "The water looks cold. Should I cross?")) // 10
        locations.add(Location("Along the stream", "That looks way too cold.")) // 11
        locations.add(Location("A bridge", "Hey, a bridge.")) // 12
        locations.add(Location("A field of flowers", "AHH, Pollen!")) // 13
        locations.add(Location("A big tree.", "That's a big tree.")) // 14
        locations.add(Location("A second forest", "Another Forest that's big and dark. Kind of looks like a magic forest.")) // 15
        locations.add(Location("Signs", "Two signs, ones points to the north saying this way while the other says Ruins")) // 16
        locations.add(Location("More forest", "")) // 17
        locations.add(Location("Building", "A building, should I go in?")) // 18
        locations.add(Location("Entrence", "This building is like a mansion")) // 19
        locations.add(Location("Hallway", "This hallway is really long")) // 20
        locations.add(Location("Sus Door way", "This looks sus.")) // 21
        locations.add(Location("A traps been set off", "The way back has been cut off, I can't go back now. Hope there wasn't anything I needed over there.")) // 22
        locations.add(Location("A hallway", "There's a hallway, one way has an exit sign, the other has a weird glow to it.")) // 23
        locations.add(Location("I hear something", "A soft buzzing. The exit?")) // 24
        locations.add(Location("A door", "The exit.")) // 25
        locations.add(Location("The End", "", "", true)) // 26

        // Maze part
        locations.add(Location("Dead end", "")) // 27
        locations.add(Location("Dead end", "")) // 28
        locations.add(Location("Oop, You're dead", "You feel into a pit trap with spikes, try again.")) // 29
        locations.add(Location("Dead end", "")) // 30
        locations.add(Location("Dead end", "")) // 31
        locations.add(Location("A tunnel", "A really dark and spooky tunnel. Looks hunted.")) // 32
        locations.add(Location("Really dark", "I can not see.")) // 33
        locations.add(Location("A chest", "Oh, a chest. Hey, there's a key inside, wonder where it goes?", "Key")) // 34
        locations.add(Location("Ruins", "Some old ruins, looks pretty unstable, I shouldn't go over there.")) // 35
        locations.add(Location("Inside the ruins", "Where's the fun in being safe :). Oh, this looks cool. A stone tablet, can't read it but still.", "Tablet")) // 36
        locations.add(Location("Dead end", "")) // 37
        locations.add(Location("Oop, You're dead", "You feel off a cliff.")) // 38
        locations.add(Location("Oop, You're dead", "You feel into a bush of poison ivy and died to it.")) // 39
        locations.add(Location("A cross the river", "Oh, that was cold, so very cold!")) // 40
        locations.add(Location("Edge of a pit", "A pit, it's deep. I think I see something down there")) // 41
        locations.add(Location("A chest", "A chest? *Opens chest* And it has a blank map in it. Okay.", "Blank Map")) // 42
        locations.add(Location("Dead", "You fell down the pit and fell into a spike trap. Try again.")) // 43
        locations.add(Location("A portal", "What happens when I go threw?")) // 44



        // The Path to the door
        // Forest to woods
        locations[0].link("N", 1)
        locations[1].link("S", 0)

        // Woods to path
        locations[1].link("E", 2)
        locations[2].link("W", 1)

        // Up the path
        locations[2].link("N", 3)
        locations[3].link("S", 2)

        locations[3].link("E", 4)
        locations[4].link("W", 3)

        // First split
        locations[4].link("E", 5)
        locations[5].link("W", 4)

        // The room
        locations[5].link("E", 6)
        locations[6].link("W", 5)

        locations[5].link("N", 7)
        locations[7].link("S", 5)

        // Hill
        locations[7].link("N", 8)
        locations[8].link("S", 7)

        locations[8].link("W", 9)
        locations[9].link("E", 8)

        // The Stream
        locations[9].link("N", 10)
        locations[10].link("S", 9)

        // Walking along the stream
        locations[10].link("W", 11)
        locations[11].link("E", 10)

        // Bridge
        locations[11].link("N", 12)
        locations[12].link("S", 11)


        locations[12].link("W", 13)
        locations[13].link("E", 12)

        locations[13].link("N", 14)
        locations[14].link("S", 13)

        locations[14].link("E", 15)
        locations[15].link("W", 14)

        locations[15].link("N", 16)
        locations[16].link("S", 15)

        locations[16].link("W", 17)
        locations[17].link("E", 16)

        locations[17].link("S", 18)
        locations[18].link("N", 17)

        locations[18].link("W", 19)
        locations[19].link("E", 18)

        locations[19].link("N", 20)
        locations[20].link("S", 19)

        locations[20].link("W", 21)
        locations[21].link("E", 20)

        locations[21].link("N", 22)

        locations[22].link("E", 23)
        locations[23].link("W", 22)

        locations[23].link("S", 24)
        locations[24].link("N", 23)

        locations[24].link("E", 25)
        locations[25].link("W", 24)

        locations[25].link("N", 26)
        locations[26].link("S", 25)

        // Maze part
        locations[19].link("W", 27)
        locations[27].link("E", 19)

        locations[8].link("E", 28)
        locations[28].link("W", 8)

        locations[8].link("E", 29)

        locations[14].link("N", 30)
        locations[30].link("S", 14)

        locations[30].link("W", 31)
        locations[31].link("E", 30)

        locations[14].link("N", 32)
        locations[32].link("S", 14)
        locations[32].link("W", 33)
        locations[33].link("E", 32)
        locations[33].link("S", 34)
        locations[34].link("N", 33)

        locations[19].link("W", 34)
        locations[34].link("E", 19)

        locations[16].link("N", 35)
        locations[35].link("S", 16)
        locations[35].link("W", 36)
        locations[36].link("E", 35)

        locations[17].link("W", 37)
        locations[37].link("E", 17)

        locations[13].link("S", 38)

        locations[18].link("W", 39)
        locations[39].link("E", 18)

        locations[10].link("N", 40)
        locations[40].link("S", 10)
        locations[40].link("E", 41)
        locations[41].link("W", 40)
        locations[41].link("N", 42)
        locations[42].link("S", 41)

        locations[41].link("E", 43)

        locations[23].link("N", 44)
        locations[44].link("S", 23)
        locations[44].link("W", 0)

    
    }


    /**
     * Configure the main window
     */
    private fun setupWindow() {
        title = "Ever Changing Maze Game"
        contentPane.preferredSize = Dimension(600, 340)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        layout = null

        pack()
    }


    /**
     * Populate the UI
     */

    private fun buildUI() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 20)

        nameLabel = JLabel("Move to start...", SwingConstants.CENTER)
        nameLabel.bounds = Rectangle(180, 30, 240, 40)
        nameLabel.font = baseFont
        add(nameLabel)

        descLabel = JLabel("Move to start...", SwingConstants.CENTER)
        descLabel.bounds = Rectangle(180, 100, 240, 100)
        descLabel.font = baseFont
        add(descLabel)

        // Add the inventory label
        inventoryLabel = JLabel("Inventory: Empty", SwingConstants.CENTER)
        inventoryLabel.bounds = Rectangle(180, 200, 240, 40)
        inventoryLabel.font = baseFont
        add(inventoryLabel)

        northButton = JButton("N")
        northButton.bounds = Rectangle(70,170,40,40)
        northButton.font = baseFont
        northButton.addActionListener(this)
        add(northButton)

        westButton = JButton("W")
        westButton.bounds = Rectangle(15,220,40,40)
        westButton.font = baseFont
        westButton.addActionListener(this)
        add(westButton)

        southButton = JButton("S")
        southButton.bounds = Rectangle(70,275,40,40)
        southButton.font = baseFont
        southButton.addActionListener(this)
        add(southButton)

        eastButton = JButton("E")
        eastButton.bounds = Rectangle(125,220,40,40)
        eastButton.font = baseFont
        eastButton.addActionListener(this)
        add(eastButton)
    }

    fun updateUI() {
        val locationToShow = locations[currentLocationIndex]

        // Check for an item in the current location
        locationToShow.item?.let {
            playerInventory.addItem(it)  // Add item to inventory
        }

        // Update the inventory display
        inventoryLabel.text = "<html>" + playerInventory.displayItems()

        nameLabel.text = locationToShow.name

        // Check if we are at the end of the maze
        if (locationToShow.isEnd == true) {
            // Yes, at the end. Have we collected everything?
            if (playerInventory.countItems() == 4) {
                descLabel.text = "<html>Congratulations! You have found the exit and you collected all the items you needed."
            }
            else {
                descLabel.text = "<html>You have reached the end, but... There are still some items you need to find :-("
            }
        }
        else {
            // Not the end, so show the normal description
            descLabel.text = "<html>" + locationToShow.description
        }

        if (locationToShow.north == -1) {
            northButton.isEnabled = false
        }
        else {
            northButton.isEnabled = true
        }

        if (locationToShow.east == -1) {
            eastButton.isEnabled = false
        }
        else {
            eastButton.isEnabled = true
        }

        if (locationToShow.south == -1) {
            southButton.isEnabled = false
        }
        else {
            southButton.isEnabled = true
        }

        if (locationToShow.west == -1) {
            westButton.isEnabled = false
        }
        else {
            westButton.isEnabled = true
        }
    }

    /**
     * Handle any UI events
     */
    override fun actionPerformed(e: ActionEvent?) {
        when (e?.source) {
            northButton -> northAction()
            westButton -> westAction()
            southButton -> southAction()
            eastButton -> eastAction()
        }
    }

    /**
     * An Example Action
     */
    private fun northAction() {
        currentLocationIndex = locations[currentLocationIndex].north
        updateUI()
    }
    private fun westAction() {
        currentLocationIndex = locations[currentLocationIndex].west
        updateUI()
    }
    private fun southAction() {
        currentLocationIndex = locations[currentLocationIndex].south
        updateUI()
    }
    private fun eastAction() {
        currentLocationIndex = locations[currentLocationIndex].east
        updateUI()
    }
}


//=============================================================================================

/**
 * Launch the application
 */
fun main() {
    // Flat, Dark look-and-feel
    FlatDarkLaf.setup()

    // Create the UI
    GUI()

}


