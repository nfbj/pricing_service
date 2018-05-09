package com.netflix.pricing.pricingservice.domain;

import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.springframework.util.StringUtils;

public class CountryCurrencyUtil {

  public static final Set<String> ISO_COUNTRIES =
      new HashSet<String>(Arrays.asList(Locale.getISOCountries()));

  public static final Set<String> ISO_CURRENCIES = new HashSet<String>(Arrays.asList("JPY", "CNY",
      "SDG", "RON", "MKD", "MXN", "CAD", "ZAR", "AUD", "NOK", "ILS", "ISK", "SYP", "LYD", "UYU",
      "YER", "CSD", "EEK", "THB", "IDR", "LBP", "AED", "BOB", "QAR", "BHD", "HNL", "HRK", "COP",
      "ALL", "DKK", "MYR", "SEK", "RSD", "BGN", "DOP", "KRW", "LVL", "VEF", "CZK", "TND", "KWD",
      "VND", "JOD", "NZD", "PAB", "CLP", "PEN", "GBP", "DZD", "CHF", "RUB", "UAH", "ARS", "SAR",
      "EGP", "INR", "PYG", "TWD", "TRY", "BAM", "OMR", "SGD", "MAD", "BYR", "NIO", "HKD", "LTL",
      "SKK", "GTQ", "BRL", "EUR", "HUF", "IQD", "CRC", "PHP", "SVC", "PLN", "USD", "XBB", "XBC",
      "XBD", "UGX", "MOP", "SHP", "TTD", "UYI", "KGS", "DJF", "BTN", "XBA", "HTG", "BBD", "XAU",
      "FKP", "MWK", "PGK", "XCD", "COU", "RWF", "NGN", "BSD", "XTS", "TMT", "GEL", "VUV", "FJD",
      "MVR", "AZN", "MNT", "MGA", "WST", "KMF", "GNF", "SBD", "BDT", "MMK", "TJS", "CVE", "MDL",
      "KES", "SRD", "LRD", "MUR", "CDF", "BMD", "USN", "CUP", "USS", "GMD", "UZS", "CUC", "ZMK",
      "NPR", "NAD", "LAK", "SZL", "XDR", "BND", "TZS", "MXV", "LSL", "KYD", "LKR", "ANG", "PKR",
      "SLL", "SCR", "GHS", "ERN", "BOV", "GIP", "IRR", "XPT", "BWP", "XFU", "CLF", "ETB", "STD",
      "XXX", "XPD", "AMD", "XPF", "JMD", "MRO", "BIF", "CHW", "ZWL", "AWG", "MZN", "CHE", "XOF",
      "KZT", "BZD", "XAG", "KHR", "XAF", "GYD", "AFN", "SOS", "TOP", "AOA", "KPW"));

  public static boolean isValidCurrency(String currency) {
    if (StringUtils.isEmpty(currency)) {
      return false;
    }
    return ISO_CURRENCIES.contains(currency.toUpperCase());
  }

  public static boolean isValidCurrencyForCountry(String currency, String countryCode) {
    if (StringUtils.isEmpty(currency) || StringUtils.isEmpty(countryCode)
        || !ISO_COUNTRIES.contains(countryCode.toUpperCase())
        || !ISO_CURRENCIES.contains(currency.toUpperCase())) {
      return false;
    }
    // todo: consider JODA money, as locales for some countries could be missing
    return Currency.getInstance(new Locale("", countryCode)).getCurrencyCode().equals(currency);

  }

}
