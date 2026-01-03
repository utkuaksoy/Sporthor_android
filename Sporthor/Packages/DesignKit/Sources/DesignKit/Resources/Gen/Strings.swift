// swiftlint:disable all
// Generated using SwiftGen — https://github.com/SwiftGen/SwiftGen

import Foundation

// swiftlint:disable superfluous_disable_command file_length implicit_return prefer_self_in_static_references

// MARK: - Strings

// swiftlint:disable explicit_type_interface function_parameter_count identifier_name line_length
// swiftlint:disable nesting type_body_length type_name vertical_whitespace_opening_braces
public enum L10n {
 /// Tamam
 public static let alertButtonOK = L10n.tr("Localizable", "alertButtonOK", fallback: "Tamam")
 /// İşleminz şu anda gerçekleştirilemiyor. Lütfen daha sonra tekrar deneyiniz.
 public static let generalError = L10n.tr("Localizable", "generalError", fallback: "İşleminz şu anda gerçekleştirilemiyor. Lütfen daha sonra tekrar deneyiniz.")
 /// Goodbye
 public static let goodbye = L10n.tr("Localizable", "goodbye", fallback: "Goodbye")
 /// Hello
 public static let hello = L10n.tr("Localizable", "hello", fallback: "Hello")
 public enum Authentication {
   /// Apple ile devam edin
   public static let apple = L10n.tr("Localizable", "authentication.apple", fallback: "Apple ile devam edin")
   /// E-mail adresinle giriş yap
   public static let email = L10n.tr("Localizable", "authentication.email", fallback: "E-mail adresinle giriş yap")
   /// Facebook ile devam edin
   public static let facebook = L10n.tr("Localizable", "authentication.facebook", fallback: "Facebook ile devam edin")
   /// Google ile devam edin
   public static let google = L10n.tr("Localizable", "authentication.google", fallback: "Google ile devam edin")
   /// ve ya
   public static let veya = L10n.tr("Localizable", "authentication.veya", fallback: "ve ya")
   public enum ForgotPassword {
     /// Sıfırlama Linki Gönder
     public static let buttonTitle = L10n.tr("Localizable", "authentication.forgotPassword.buttonTitle", fallback: "Sıfırlama Linki Gönder")
     /// Sıfırlama linki mail veya sms olarak gönderilecektir.
     public static let description = L10n.tr("Localizable", "authentication.forgotPassword.description", fallback: "Sıfırlama linki mail veya sms olarak gönderilecektir.")
     /// Şifremi Unuttum
     public static let title = L10n.tr("Localizable", "authentication.forgotPassword.title", fallback: "Şifremi Unuttum")
     /// Kullanıcı Adı
     public static let username = L10n.tr("Localizable", "authentication.forgotPassword.username", fallback: "Kullanıcı Adı")
   }
   public enum Login {
     /// Henüz üye değil misin?
     public static let notAMemberYet = L10n.tr("Localizable", "authentication.login.notAMemberYet", fallback: "Henüz üye değil misin?")
   }
   public enum LoginUsername {
     /// Giriş Yap
     public static let buttonTitle = L10n.tr("Localizable", "authentication.loginUsername.buttonTitle", fallback: "Giriş Yap")
     /// Sistemde kayıtlı kullanıcı adınız ve şifrenizle giriş yapabilirsiniz.
     public static let description = L10n.tr("Localizable", "authentication.loginUsername.description", fallback: "Sistemde kayıtlı kullanıcı adınız ve şifrenizle giriş yapabilirsiniz.")
     /// Şifremi Unuttum
     public static let forgotPassword = L10n.tr("Localizable", "authentication.loginUsername.forgotPassword", fallback: "Şifremi Unuttum")
     /// Hemen Üye Olun
     public static let register = L10n.tr("Localizable", "authentication.loginUsername.register", fallback: "Hemen Üye Olun")
     /// Giriş Yap
     public static let title = L10n.tr("Localizable", "authentication.loginUsername.title", fallback: "Giriş Yap")
   }
   public enum PersonalInformation {
     /// Devam Et
     public static let buttonTitle = L10n.tr("Localizable", "authentication.personalInformation.buttonTitle", fallback: "Devam Et")
     /// Harika! Şimdi kişisel bilgilerini girerek üyeliğini oluşturalım.
     public static let description = L10n.tr("Localizable", "authentication.personalInformation.description", fallback: "Harika! Şimdi kişisel bilgilerini girerek üyeliğini oluşturalım.")
     /// E-mail adresi
     public static let email = L10n.tr("Localizable", "authentication.personalInformation.email", fallback: "E-mail adresi")
     /// İsim
     public static let name = L10n.tr("Localizable", "authentication.personalInformation.name", fallback: "İsim")
     /// Şifre
     public static let password = L10n.tr("Localizable", "authentication.personalInformation.password", fallback: "Şifre")
     /// Güçlü
     public static let statusCorrect = L10n.tr("Localizable", "authentication.personalInformation.statusCorrect", fallback: "Güçlü")
     /// Parola Durumu
     public static let statusTitle = L10n.tr("Localizable", "authentication.personalInformation.statusTitle", fallback: "Parola Durumu")
     /// Güçsüz
     public static let statusWrong = L10n.tr("Localizable", "authentication.personalInformation.statusWrong", fallback: "Güçsüz")
     /// Soyisim
     public static let surname = L10n.tr("Localizable", "authentication.personalInformation.surname", fallback: "Soyisim")
     /// Kişisel Bilgiler
     public static let title = L10n.tr("Localizable", "authentication.personalInformation.title", fallback: "Kişisel Bilgiler")
   }
   public enum Phone {
     public enum Login {
       /// Giriş Yap
       public static let title = L10n.tr("Localizable", "authentication.phone.login.title", fallback: "Giriş Yap")
     }
     public enum Register {
       /// Doğrulama Kodu Gönder
       public static let buttonTitle = L10n.tr("Localizable", "authentication.phone.register.buttonTitle", fallback: "Doğrulama Kodu Gönder")
       /// Telefon numaranızı girin. Numaranızı onaylamak için kısa mesaj göndereceğiz.
       public static let description = L10n.tr("Localizable", "authentication.phone.register.description", fallback: "Telefon numaranızı girin. Numaranızı onaylamak için kısa mesaj göndereceğiz.")
       /// Hadi Başlayalım!
       public static let title = L10n.tr("Localizable", "authentication.phone.register.title", fallback: "Hadi Başlayalım!")
     }
   }
   public enum PhoneCell {
     /// ve ya
     public static let lineTitle = L10n.tr("Localizable", "authentication.phoneCell.lineTitle", fallback: "ve ya")
     /// Telefon Numarası
     public static let title = L10n.tr("Localizable", "authentication.phoneCell.title", fallback: "Telefon Numarası")
     public enum AreaTextField {
       /// +90
       public static let text = L10n.tr("Localizable", "authentication.phoneCell.areaTextField.text", fallback: "+90")
     }
     public enum PhoneTextField {
       /// Telefon Numaranız
       public static let placeholder = L10n.tr("Localizable", "authentication.phoneCell.phoneTextField.placeholder", fallback: "Telefon Numaranız")
     }
   }
   public enum Register {
     /// Zaten bir hesabın var mı?
     public static let haveAnAccount = L10n.tr("Localizable", "authentication.register.haveAnAccount", fallback: "Zaten bir hesabın var mı?")
   }
   public enum Team {
     /// Devam Et
     public static let buttonTitle = L10n.tr("Localizable", "authentication.team.buttonTitle", fallback: "Devam Et")
     /// Oynadığın takımı seç kulüp koordinatörü onayladığında topluluğa katıl.
     public static let description = L10n.tr("Localizable", "authentication.team.description", fallback: "Oynadığın takımı seç kulüp koordinatörü onayladığında topluluğa katıl.")
     /// Takımını Ara
     public static let searchPlaceholder = L10n.tr("Localizable", "authentication.team.searchPlaceholder", fallback: "Takımını Ara")
     /// Hangi takımda oynuyorsun?
     public static let title = L10n.tr("Localizable", "authentication.team.title", fallback: "Hangi takımda oynuyorsun?")
   }
   public enum Username {
     /// @ kullanıcı adıyla devam edebilirsin
     public static let correctStatus = L10n.tr("Localizable", "authentication.username.correctStatus", fallback: "@ kullanıcı adıyla devam edebilirsin")
     /// Bu, insanların Sporthor’da sizi tanıyacağı isimdir.
     public static let description = L10n.tr("Localizable", "authentication.username.description", fallback: "Bu, insanların Sporthor’da sizi tanıyacağı isimdir.")
     /// Geçerli bir kullanıcı adı girmek için 4 ile 15 karakter arasında sadece küçük harf, rakam, nokta(.) veya alt çizgi(_) kullanabilirsiniz.
     public static let regexWarning = L10n.tr("Localizable", "authentication.username.regexWarning", fallback: "Geçerli bir kullanıcı adı girmek için 4 ile 15 karakter arasında sadece küçük harf, rakam, nokta(.) veya alt çizgi(_) kullanabilirsiniz.")
     /// Kullanıcı Adı Önerileri
     public static let suggestionHeader = L10n.tr("Localizable", "authentication.username.suggestionHeader", fallback: "Kullanıcı Adı Önerileri")
     /// Kullanıcı Adı
     public static let title = L10n.tr("Localizable", "authentication.username.title", fallback: "Kullanıcı Adı")
     /// @ başkası tarafından alınmış
     public static let wrongStatus = L10n.tr("Localizable", "authentication.username.wrongStatus", fallback: "@ başkası tarafından alınmış")
   }
   public enum VerifyCode {
     /// Numaranı Doğrula
     public static let buttonTitle = L10n.tr("Localizable", "authentication.verifyCode.buttonTitle", fallback: "Numaranı Doğrula")
     /// @ 'nolu telefon numarana doğrulama kodu gönderdik
     public static let description = L10n.tr("Localizable", "authentication.verifyCode.description", fallback: "@ 'nolu telefon numarana doğrulama kodu gönderdik")
     /// Kodu Yeniden Gönder
     public static let sendAgainButton = L10n.tr("Localizable", "authentication.verifyCode.sendAgainButton", fallback: "Kodu Yeniden Gönder")
     /// Doğrulama Kodu
     public static let title = L10n.tr("Localizable", "authentication.verifyCode.title", fallback: "Doğrulama Kodu")
   }
 }
 public enum Chat {
   public enum TextField {
     public enum Hint {
       /// Mesajınızı yazın...
       public static let title = L10n.tr("Localizable", "chat.TextField.Hint.Title", fallback: "Mesajınızı yazın...")
     }
   }
   public enum Button {
     /// İptal
     public static let cancel = L10n.tr("Localizable", "chat.button.cancel", fallback: "İptal")
     /// Tamam
     public static let ok = L10n.tr("Localizable", "chat.button.ok", fallback: "Tamam")
     /// Ayarlar
     public static let settings = L10n.tr("Localizable", "chat.button.settings", fallback: "Ayarlar")
   }
   public enum Error {
     /// Kamera kullanımı için izin vermeniz gerekmektedir. Ayarlar'dan izin verebilirsiniz.
     public static let cameraPermissionMessage = L10n.tr("Localizable", "chat.error.cameraPermissionMessage", fallback: "Kamera kullanımı için izin vermeniz gerekmektedir. Ayarlar'dan izin verebilirsiniz.")
     /// Kamera İzni Gerekli
     public static let cameraPermissionTitle = L10n.tr("Localizable", "chat.error.cameraPermissionTitle", fallback: "Kamera İzni Gerekli")
     /// Bağlantı başarısız oldu
     public static let connectionFailed = L10n.tr("Localizable", "chat.error.connectionFailed", fallback: "Bağlantı başarısız oldu")
     /// Bağlantı kurulamadı. Lütfen daha sonra tekrar deneyin.
     public static let connectionRetryFailed = L10n.tr("Localizable", "chat.error.connectionRetryFailed", fallback: "Bağlantı kurulamadı. Lütfen daha sonra tekrar deneyin.")
     /// Dosya işlenirken bir hata oluştu: %@
     public static func fileProcessingError(_ p1: Any) -> String {
      return L10n.tr("Localizable", "chat.error.fileProcessingError", String(describing: p1), fallback: "Dosya işlenirken bir hata oluştu: %@")
     }
     /// Dosya boyutu çok büyük. Maximum 50MB yükleyebilirsiniz.
     public static let fileSizeTooLarge = L10n.tr("Localizable", "chat.error.fileSizeTooLarge", fallback: "Dosya boyutu çok büyük. Maximum 50MB yükleyebilirsiniz.")
     /// Bu dosya türü desteklenmiyor.
     public static let fileTypeNotSupported = L10n.tr("Localizable", "chat.error.fileTypeNotSupported", fallback: "Bu dosya türü desteklenmiyor.")
     /// Fotoğraf seçmek için galeri izni vermeniz gerekmektedir. Ayarlar'dan izin verebilirsiniz.
     public static let galleryPermissionMessage = L10n.tr("Localizable", "chat.error.galleryPermissionMessage", fallback: "Fotoğraf seçmek için galeri izni vermeniz gerekmektedir. Ayarlar'dan izin verebilirsiniz.")
     /// Galeri İzni Gerekli
     public static let galleryPermissionTitle = L10n.tr("Localizable", "chat.error.galleryPermissionTitle", fallback: "Galeri İzni Gerekli")
     /// Mesaj gönderilemedi. Lütfen tekrar deneyin.
     public static let messageSendFailed = L10n.tr("Localizable", "chat.error.messageSendFailed", fallback: "Mesaj gönderilemedi. Lütfen tekrar deneyin.")
     /// Bağlantı koptu. Yeniden bağlanılıyor...
     public static let reconnecting = L10n.tr("Localizable", "chat.error.reconnecting", fallback: "Bağlantı koptu. Yeniden bağlanılıyor...")
     /// Chat servisi başlatılamadı
     public static let serviceInitFailed = L10n.tr("Localizable", "chat.error.serviceInitFailed", fallback: "Chat servisi başlatılamadı")
     /// Hata
     public static let title = L10n.tr("Localizable", "chat.error.title", fallback: "Hata")
     /// Video işlenirken bir hata oluştu.
     public static let videoProcessingError = L10n.tr("Localizable", "chat.error.videoProcessingError", fallback: "Video işlenirken bir hata oluştu.")
     /// Video boyutu çok büyük. Lütfen daha kısa bir video seçin.
     public static let videoSizeTooLarge = L10n.tr("Localizable", "chat.error.videoSizeTooLarge", fallback: "Video boyutu çok büyük. Lütfen daha kısa bir video seçin.")
   }
   public enum Message {
     /// 📎 Dosya yüklenemedi
     public static let fileLoadFailed = L10n.tr("Localizable", "chat.message.fileLoadFailed", fallback: "📎 Dosya yüklenemedi")
     /// 🖼 Resim yüklenemedi
     public static let imageLoadFailed = L10n.tr("Localizable", "chat.message.imageLoadFailed", fallback: "🖼 Resim yüklenemedi")
     /// 🎥 Video yüklenemedi
     public static let videoLoadFailed = L10n.tr("Localizable", "chat.message.videoLoadFailed", fallback: "🎥 Video yüklenemedi")
   }
 }
 public enum Experience {
   public enum BirthdateAndGender {
     /// Doğum tarihini gir
     public static let birthDateTitle = L10n.tr("Localizable", "experience.birthdateAndGender.birthDateTitle", fallback: "Doğum tarihini gir")
     /// Bitir
     public static let buttonTitle = L10n.tr("Localizable", "experience.birthdateAndGender.buttonTitle", fallback: "Bitir")
     /// Cinsiyet
     public static let gender = L10n.tr("Localizable", "experience.birthdateAndGender.gender", fallback: "Cinsiyet")
     /// Harika! Artık son adımdasın.
     public static let title = L10n.tr("Localizable", "experience.birthdateAndGender.title", fallback: "Harika! Artık son adımdasın.")
     public enum BirthDate {
       /// Gün
       public static let day = L10n.tr("Localizable", "experience.birthdateAndGender.birthDate.day", fallback: "Gün")
       /// Ay
       public static let month = L10n.tr("Localizable", "experience.birthdateAndGender.birthDate.month", fallback: "Ay")
       /// Yıl
       public static let year = L10n.tr("Localizable", "experience.birthdateAndGender.birthDate.year", fallback: "Yıl")
     }
   }
   public enum Branch {
     /// Devam Et
     public static let buttonTitle = L10n.tr("Localizable", "experience.branch.buttonTitle", fallback: "Devam Et")
     /// * En az birini seçerek devam edebilirsin
     public static let description = L10n.tr("Localizable", "experience.branch.description", fallback: "* En az birini seçerek devam edebilirsin")
     /// Hangi sporla ilgileniyorsun?
     public static let title = L10n.tr("Localizable", "experience.branch.title", fallback: "Hangi sporla ilgileniyorsun?")
   }
   public enum Job {
     /// Devam Et
     public static let buttonTitle = L10n.tr("Localizable", "experience.job.buttonTitle", fallback: "Devam Et")
     /// * En az birini seçerek devam edebilirsin
     public static let description = L10n.tr("Localizable", "experience.job.description", fallback: "* En az birini seçerek devam edebilirsin")
     /// Hangisi senin için uygun?
     public static let title = L10n.tr("Localizable", "experience.job.title", fallback: "Hangisi senin için uygun?")
   }
   public enum Main {
     /// Hemen Başlayalım
     public static let buttonTitle = L10n.tr("Localizable", "experience.main.buttonTitle", fallback: "Hemen Başlayalım")
     /// Deneyimini kişiselleştirmek için sana bir kaç sorumuz var.
     public static let title = L10n.tr("Localizable", "experience.main.title", fallback: "Deneyimini kişiselleştirmek için sana bir kaç sorumuz var.")
   }
 }
 public enum NextOnboarding {
   /// Devam Et
   public static let continueButtonTitle = L10n.tr("Localizable", "nextOnboarding.continueButtonTitle", fallback: "Devam Et")
   /// Haydi Başlayalım
   public static let lastElementButtonTitle = L10n.tr("Localizable", "nextOnboarding.lastElementButtonTitle", fallback: "Haydi Başlayalım")
 }
 public enum Onboarding {
   /// Sporun ve sporcunun yerli ve ilk dijital ekosistemine hoş geldiniz
   public static let description = L10n.tr("Localizable", "onboarding.description", fallback: "Sporun ve sporcunun yerli ve ilk dijital ekosistemine hoş geldiniz")
   /// Uygulamayı Keşfet
   public static let discoverTheApp = L10n.tr("Localizable", "onboarding.discoverTheApp", fallback: "Uygulamayı Keşfet")
   /// Giriş Yap
   public static let login = L10n.tr("Localizable", "onboarding.login", fallback: "Giriş Yap")
   /// Kayıt Ol
   public static let register = L10n.tr("Localizable", "onboarding.register", fallback: "Kayıt Ol")
   /// Sporthor'a Hoş geldin
   public static let title = L10n.tr("Localizable", "onboarding.title", fallback: "Sporthor'a Hoş geldin")
 }
 public enum PersonsPermission {
   /// Devam Et
   public static let buttonTitle = L10n.tr("Localizable", "personsPermission.buttonTitle", fallback: "Devam Et")
   /// Tam Erişime İzin Ver ve Sporthor’dan arkadaş önerileri al ve ya “Kişileri Seç” seçeneğini işaretle ve kişilerin otomatik şekilde arkadaş olarak eklensin.
   public static let description = L10n.tr("Localizable", "personsPermission.description", fallback: "Tam Erişime İzin Ver ve Sporthor’dan arkadaş önerileri al ve ya “Kişileri Seç” seçeneğini işaretle ve kişilerin otomatik şekilde arkadaş olarak eklensin.")
   /// Tüm arkadaşlarını bul
   public static let title = L10n.tr("Localizable", "personsPermission.title", fallback: "Tüm arkadaşlarını bul")
 }
 public enum Settings {
   /// Ayarlar
   public static let title = L10n.tr("Localizable", "settings.Title", fallback: "Ayarlar")
   public enum About {
     /// Hakkında
     public static let title = L10n.tr("Localizable", "settings.about.title", fallback: "Hakkında")
   }
   public enum AccountPrivacy {
     /// Hesap Gizliliği
     public static let title = L10n.tr("Localizable", "settings.accountPrivacy.title", fallback: "Hesap Gizliliği")
   }
   public enum ApplicationPreferences {
     /// Uygulama Tercihleri
     public static let title = L10n.tr("Localizable", "settings.applicationPreferences.title", fallback: "Uygulama Tercihleri")
   }
   public enum ContactCenter {
     /// İletişim Merkezi
     public static let title = L10n.tr("Localizable", "settings.contactCenter.title", fallback: "İletişim Merkezi")
   }
   public enum Footer {
     /// Sporthor © 2025. Tüm hakları saklıdır
     public static let title = L10n.tr("Localizable", "settings.footer.title", fallback: "Sporthor © 2025. Tüm hakları saklıdır")
   }
   public enum Help {
     /// Yardım Alın
     public static let title = L10n.tr("Localizable", "settings.help.title", fallback: "Yardım Alın")
   }
   public enum Logout {
     /// Oturumu Kapat
     public static let title = L10n.tr("Localizable", "settings.logout.title", fallback: "Oturumu Kapat")
   }
 }
}
// swiftlint:enable explicit_type_interface function_parameter_count identifier_name line_length
// swiftlint:enable nesting type_body_length type_name vertical_whitespace_opening_braces

// MARK: - Implementation Details

extension L10n {
 private static func tr(_ table: String, _ key: String, _ args: CVarArg..., fallback value: String) -> String {
  let format = BundleToken.bundle.localizedString(forKey: key, value: value, table: table)
  return String(format: format, arguments: args)
 }
}

// swiftlint:disable convenience_type
private final class BundleToken {
 static let bundle: Bundle = {
  #if SWIFT_PACKAGE
  return Bundle.module
  #else
  return Bundle(for: BundleToken.self)
  #endif
 }()
}
// swiftlint:enable convenience_type
