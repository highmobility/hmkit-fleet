/*
 * The MIT License
 *
 * Copyright (c) 2014@SerialName("High")-Mobility GmbH (https://high-mobility.com)
 * ),
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
@file:UseSerializers(AccessCertificateSerializer::class)

package com.highmobility.hmkitfleet.model

import com.highmobility.crypto.AccessCertificate
import com.highmobility.value.Bytes
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class VehicleAccess(
    val vin: String,
    val accessToken: AccessToken,
    val accessCertificate: AccessCertificate
)

@Serializer(forClass = AccessCertificate::class)
internal object AccessCertificateSerializer : KSerializer<AccessCertificate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AccessCertificate) {
        encoder.encodeString(value.hex)
    }

    override fun deserialize(decoder: Decoder): AccessCertificate {
        return AccessCertificate(Bytes(decoder.decodeString()))
    }
}

@Serializable
enum class Brand {
    @SerialName("bmw")
    BMW,

    @SerialName("citroen")
    CITROEN,

    @SerialName("ds")
    DS,

    @SerialName("mercedes-benz")
    MERCEDES_BENZ,

    @SerialName("mini")
    MINI,

    @SerialName("opel")
    OPEL,

    @SerialName("peugeot")
    PEUGEOT,

    @SerialName("vauxhall")
    VAUXHALL,

    @SerialName("jeep")
    JEEP,

    @SerialName("fiat")
    FIAT,

    @SerialName("alfaromeo")
    ALFAROMEO,

    @SerialName("ford")
    FORD,

    @SerialName("renault")
    RENAULT,

    @SerialName("toyota")
    TOYOTA,

    @SerialName("lexus")
    LEXUS,

    @SerialName("sandbox")
    SANDBOX
}