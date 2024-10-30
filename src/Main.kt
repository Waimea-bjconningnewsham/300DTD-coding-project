/**
 * ------------------------------------------------------------------------
 * PROJECT NAME HERE
 * Level 2 programming project
 *
 * by Brianna Conning-Newsham
 *
 * I'm making an ever-changing maze, the maze will randomly generate.
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
class Location(val location: String)
/**
 * GUI class
 * Defines the UI and responds to events
 */
class GUI : JFrame(), ActionListener {

    val locations = mutableListOf<Location>()
    var currentLocationIdex: Int

    // Setup some properties to hold the UI elements
    private lateinit var exampleLabel: JLabel
    private lateinit var northButton: JButton
    private lateinit var westButton: JButton
    private lateinit var southButton: JButton
    private lateinit var eastButton: JButton


    /**
     * Create, build and run the UI
     */
    init {
        setupWindow()
        buildUI()


        // Show the app, centred on screen
        setLocationRelativeTo(null)
        isVisible = true

        // Show first location
        currentLocationIdex = 0
        updateUI()
    }

    fun setupData() {
        locations.add(Location("Forest"))
        locations.add(Location("Woods"))
        locations.add(Location("Path"))
        locations.add(Location("South of Path"))
        locations.add(Location("North of Path"))
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

        exampleLabel = JLabel("Move to start...", SwingConstants.CENTER)
        exampleLabel.bounds = Rectangle(180, 130, 240, 40)
        exampleLabel.font = baseFont
        add(exampleLabel)

        northButton = JButton("North")
        northButton.bounds = Rectangle(260,0,80,40)
        northButton.font = baseFont
        northButton.addActionListener(this)
        add(northButton)

        westButton = JButton("West")
        westButton.bounds = Rectangle(500,130,80,40)
        westButton.font = baseFont
        westButton.addActionListener(this)
        add(westButton)

        southButton = JButton("South")
        southButton.bounds = Rectangle(260,290,80,40)
        southButton.font = baseFont
        southButton.addActionListener(this)
        add(southButton)

        eastButton = JButton("East")
        eastButton.bounds = Rectangle(20,140,80,40)
        eastButton.font = baseFont
        eastButton.addActionListener(this)
        add(eastButton)
    }

    fun updateUI() {
        val locationToShow = locations[currentLocationIdex]

        exampleLabel.text = locationToShow.location

        if (currentLocationIdex == locations.size - 1) {
            northButton.isEnabled = false
            southButton.isEnabled = false
            eastButton.isEnabled = false
            westButton.isEnabled = false
        }
        else {
            northButton.isEnabled = true

        }

        if (currentLocationIdex == 0) {
            northButton.isEnabled = false
            southButton.isEnabled = false
            eastButton.isEnabled = false
            westButton.isEnabled = false
        }
        else{
            northButton.isEnabled = true
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
        currentLocationIdex++
        updateUI()
    }
    private fun westAction() {
        exampleLabel.text = "You went west"
    }
    private fun southAction() {
        exampleLabel.text = "You went south"
    }
    private fun eastAction() {
        exampleLabel.text = "You went east"
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


