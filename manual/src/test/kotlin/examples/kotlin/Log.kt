package examples.kotlin

import org.shakespeareframework.Ability
import java.util.logging.Logger

// tag::ability[]
data class Log(val name: String, val logger: Logger = Logger.getLogger(name)) : Ability
// end::ability[]
