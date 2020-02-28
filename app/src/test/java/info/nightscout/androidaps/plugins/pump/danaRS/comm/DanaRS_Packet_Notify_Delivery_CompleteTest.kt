package info.nightscout.androidaps.plugins.pump.danaRS.comm

import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import info.TestBase
import info.nightscout.androidaps.logging.AAPSLogger
import info.nightscout.androidaps.plugins.bus.RxBusWrapper
import info.nightscout.androidaps.plugins.configBuilder.ConfigBuilderPlugin
import info.nightscout.androidaps.plugins.configBuilder.ProfileFunction
import info.nightscout.androidaps.plugins.pump.danaRS.DanaRSPlugin
import info.nightscout.androidaps.plugins.treatments.Treatment
import info.nightscout.androidaps.utils.DefaultValueHelper
import info.nightscout.androidaps.utils.resources.ResourceHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyDouble
import org.mockito.Mockito.anyInt
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(RxBusWrapper::class)
class DanaRS_Packet_Notify_Delivery_CompleteTest : TestBase() {

    @Mock lateinit var defaultValueHelper: DefaultValueHelper
    @Mock lateinit var rxBus: RxBusWrapper
    @Mock lateinit var aapsLogger: AAPSLogger
    @Mock lateinit var resourceHelper: ResourceHelper
    @Mock lateinit var danaRSPlugin: DanaRSPlugin
    @Mock lateinit var profileFunction: ProfileFunction
    @Mock lateinit var configBuilderPlugin: ConfigBuilderPlugin

    private var treatmentInjector: HasAndroidInjector = HasAndroidInjector {
        AndroidInjector {
            if (it is Treatment) {
                it.defaultValueHelper = defaultValueHelper
                it.resourceHelper = resourceHelper
                it.profileFunction = profileFunction
                it.configBuilderPlugin = configBuilderPlugin
            }
        }
    }

    @Test fun runTest() {
        `when`(resourceHelper.gs(anyInt(), anyDouble())).thenReturn("SomeString")

        danaRSPlugin.bolusingTreatment = Treatment(treatmentInjector)
        val packet = DanaRS_Packet_Notify_Delivery_Complete(aapsLogger, rxBus, resourceHelper, danaRSPlugin)
        // test params
        Assert.assertEquals(null, packet.requestParams)
        // test message decoding
        packet.handleMessage(createArray(17, 0.toByte()))
        Assert.assertEquals(true, danaRSPlugin.bolusDone)
        Assert.assertEquals("NOTIFY__DELIVERY_COMPLETE", packet.friendlyName)
    }

    fun createArray(length: Int, fillWith: Byte): ByteArray {
        val ret = ByteArray(length)
        for (i in 0 until length) {
            ret[i] = fillWith
        }
        return ret
    }

    fun createArray(length: Int, fillWith: Double): DoubleArray {
        val ret = DoubleArray(length)
        for (i in 0 until length) {
            ret[i] = fillWith
        }
        return ret
    }
}