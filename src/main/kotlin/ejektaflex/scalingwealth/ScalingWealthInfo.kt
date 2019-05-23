package ejektaflex.scalingwealth

object ScalingWealthInfo {
    const val ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"
    const val MODID = "scalingwealth"
    const val NAME = "Scaling Wealth"
    const val VERSION = "1.0.0"

    const val FORGE_DEP = "required-after:forge@[14.23.4.2768,15.0.0.0);"
    const val DEPENDS = "${FORGE_DEP}required-after:forgelin@[1.8.0,1.9.0);" +
            "required-after:silentlib;" +
            "required-after:forgelin;" +
            "required-after:scalinghealth;"

    const val CLIENT = "ejektaflex.scalingwealth.proxy.ClientProxy"
    const val SERVER = "ejektaflex.scalingwealth.proxy.CommonProxy"
}
