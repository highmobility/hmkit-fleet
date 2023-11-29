package com.highmobility.hmkitfleet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

  @SerialName("porsche")
  PORSCHE,

  @SerialName("maserati")
  MASERATI,

  @SerialName("kia")
  KIA,

  @SerialName("tesla")
  TESLA,

  @SerialName("sandbox")
  SANDBOX,
}
