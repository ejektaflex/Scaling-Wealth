package ejektaflex.scalingwealth.compat

import mcjty.theoneprobe.api.ITheOneProbe

class GetTheOneProbe : Function<ITheOneProbe> {

    fun apply(theOneProbe: ITheOneProbe) {
        println("GRABBING PROBE!")
        probe = theOneProbe
        theOneProbe.registerEntityProvider(ScalingTOPAddon())
    }

    companion object {
        var probe: ITheOneProbe? = null
    }

}