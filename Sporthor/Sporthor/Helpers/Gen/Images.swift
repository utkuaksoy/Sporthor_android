// swiftlint:disable all
// Generated using SwiftGen — https://github.com/SwiftGen/SwiftGen

#if os(macOS)
  import AppKit
#elseif os(iOS)
  import UIKit
#elseif os(tvOS) || os(watchOS)
  import UIKit
#endif
#if canImport(SwiftUI)
  import SwiftUI
#endif

// Deprecated typealiases
@available(*, deprecated, renamed: "ColorAsset.Color", message: "This typealias will be removed in SwiftGen 7.0")
internal typealias AssetColorTypeAlias = ColorAsset.Color
@available(*, deprecated, renamed: "ImageAsset.Image", message: "This typealias will be removed in SwiftGen 7.0")
internal typealias AssetImageTypeAlias = ImageAsset.Image

// swiftlint:disable superfluous_disable_command file_length implicit_return

// MARK: - Asset Catalogs

// swiftlint:disable identifier_name line_length nesting type_body_length type_name
internal enum Asset {
  internal static let accentColor = ColorAsset(name: "AccentColor")
  internal static let error = ImageAsset(name: "error")
  internal static let infoAlert = ImageAsset(name: "info-alert")
  internal static let success = ImageAsset(name: "success")
  internal static let warning = ImageAsset(name: "warning")
  internal static let discover1 = ImageAsset(name: "discover1")
  internal static let discover2 = ImageAsset(name: "discover2")
  internal static let discover3 = ImageAsset(name: "discover3")
  internal static let bellBadge = ImageAsset(name: "bell-badge")
  internal static let calendarBadge = ImageAsset(name: "calendar-badge")
  internal static let calendar = ImageAsset(name: "calendar")
  internal static let headerAppIcon = ImageAsset(name: "header-app-icon")
  internal static let volumeHigh = ImageAsset(name: "volume-high")
  internal static let volumeSlash = ImageAsset(name: "volume-slash")
  internal static let comment = ImageAsset(name: "comment")
  internal static let heart = ImageAsset(name: "heart")
  internal static let like = ImageAsset(name: "like")
  internal static let send = ImageAsset(name: "send")
  internal static let searchbarClose = ImageAsset(name: "searchbar-close")
  internal static let searchbarSearch = ImageAsset(name: "searchbar-search")
  internal static let settingsAboutIcon = ImageAsset(name: "settings-about-icon")
  internal static let settingsAccountPrivacyIcon = ImageAsset(name: "settings-account-privacy-icon")
  internal static let settingsApplicationPreferencesIcon = ImageAsset(name: "settings-application-preferences-icon")
  internal static let settingsContractCenterIcon = ImageAsset(name: "settings-contract-center-icon")
  internal static let settingsFooterIcon = ImageAsset(name: "settings-footer-icon")
  internal static let settingsHelpIcon = ImageAsset(name: "settings-help-icon")
  internal static let settingsLogoutIcon = ImageAsset(name: "settings-logout-icon")
  internal static let tabbarChat = ImageAsset(name: "tabbar_chat")
  internal static let tabbarHome = ImageAsset(name: "tabbar_home")
  internal static let tabbarPlus = ImageAsset(name: "tabbar_plus")
  internal static let tabbarSearch = ImageAsset(name: "tabbar_search")
  internal static let aboutSporthor = ImageAsset(name: "about-sporthor")
  internal static let addPlusIcon = ImageAsset(name: "add-plus-icon")
  internal static let arrowRight = ImageAsset(name: "arrow-right")
  internal static let arrowTop = ImageAsset(name: "arrow-top")
  internal static let back = ImageAsset(name: "back")
  internal static let backgroundLightGreenEllipse = ImageAsset(name: "background-light-green-ellipse")
  internal static let backgroundWhiteEllipse = ImageAsset(name: "background-white-ellipse")
  internal static let bellSlashIcon = ImageAsset(name: "bell-slash-icon")
  internal static let blackPlus = ImageAsset(name: "black-plus")
  internal static let bookmarkPlus = ImageAsset(name: "bookmark-plus")
  internal static let cameraWhite = ImageAsset(name: "camera-white")
  internal static let celebrationBackground = ImageAsset(name: "celebration-background")
  internal static let celebrationBlurBackground = ImageAsset(name: "celebration-blur-background")
  internal static let change = ImageAsset(name: "change")
  internal static let characterButton = ImageAsset(name: "character-button")
  internal static let checkSuccessPrimary = ImageAsset(name: "check-success-primary")
  internal static let checkbox = ImageAsset(name: "checkbox")
  internal static let checked = ImageAsset(name: "checked")
  internal static let chevronDown = ImageAsset(name: "chevron-down")
  internal static let chevronLeftIcon = ImageAsset(name: "chevron-left-icon")
  internal static let chevronLeftWhiteIcon = ImageAsset(name: "chevron-left-white-icon")
  internal static let chevronRightGrey = ImageAsset(name: "chevron-right-grey")
  internal static let chevronRightWhiteIcon = ImageAsset(name: "chevron-right-white-icon")
  internal static let chevronRight = ImageAsset(name: "chevron-right")
  internal static let chevronUpGrey = ImageAsset(name: "chevron-up-grey")
  internal static let clockPlus = ImageAsset(name: "clock-plus")
  internal static let closeBackgroundBlack = ImageAsset(name: "close-background-black")
  internal static let closeGrey = ImageAsset(name: "close-grey")
  internal static let closeMini = ImageAsset(name: "close-mini")
  internal static let closeWhite = ImageAsset(name: "close-white")
  internal static let close = ImageAsset(name: "close")
  internal static let connectContractsIcon = ImageAsset(name: "connect-contracts-icon")
  internal static let createCommunityIcon = ImageAsset(name: "create-community-icon")
  internal static let documentSuccessIcon = ImageAsset(name: "document-success-icon")
  internal static let eczacibasi = ImageAsset(name: "eczacibasi")
  internal static let ellipsePrimaryBackground = ImageAsset(name: "ellipse-primary-background")
  internal static let emptyMessageIcon = ImageAsset(name: "empty-message-icon")
  internal static let errorAlert = ImageAsset(name: "error-alert")
  internal static let errorUserImage = ImageAsset(name: "error-user-image")
  internal static let errorimage = ImageAsset(name: "errorimage")
  internal static let exIcon = ImageAsset(name: "ex_icon")
  internal static let eyeSlash = ImageAsset(name: "eye-slash")
  internal static let eye = ImageAsset(name: "eye")
  internal static let facebook = ImageAsset(name: "facebook")
  internal static let fileUploadWhite = ImageAsset(name: "file-upload-white")
  internal static let gradientGreenOctagon = ImageAsset(name: "gradient-green-octagon")
  internal static let greyPlus = ImageAsset(name: "grey-plus")
  internal static let greyRectangle = ImageAsset(name: "grey-rectangle")
  internal static let groupIcon = ImageAsset(name: "group-icon")
  internal static let groupImagePlusIcon = ImageAsset(name: "group-image-plus-icon")
  internal static let headerAddButton = ImageAsset(name: "header-add-button")
  internal static let headerGradient = ImageAsset(name: "header-gradient")
  internal static let imagePlusIcon = ImageAsset(name: "image-plus-icon")
  internal static let imagePlus = ImageAsset(name: "image-plus")
  internal static let imageVideoIcon = ImageAsset(name: "image_video_icon")
  internal static let info = ImageAsset(name: "info")
  internal static let leaveChatIcon = ImageAsset(name: "leave-chat-icon")
  internal static let mediaImage = ImageAsset(name: "media-image")
  internal static let menu = ImageAsset(name: "menu")
  internal static let moreHoriz = ImageAsset(name: "more-horiz")
  internal static let multipleSelectedWhiteFill = ImageAsset(name: "multiple-selected-white-fill")
  internal static let multipleSelectedWhite = ImageAsset(name: "multiple-selected-white")
  internal static let multipleImageIcon = ImageAsset(name: "multiple_image_icon")
  internal static let notificationLike = ImageAsset(name: "notification-like")
  internal static let passwordError = ImageAsset(name: "password-error")
  internal static let passwordSuccess = ImageAsset(name: "password-success")
  internal static let pencilEdit = ImageAsset(name: "pencil-edit")
  internal static let pin = ImageAsset(name: "pin")
  internal static let playCircle = ImageAsset(name: "play-circle")
  internal static let privacyPolicy = ImageAsset(name: "privacy-policy")
  internal static let profileInfoBg = ImageAsset(name: "profile-info-bg")
  internal static let redTrashIcon = ImageAsset(name: "red-trash-icon")
  internal static let rpe = ImageAsset(name: "rpe")
  internal static let selectableBlack = ImageAsset(name: "selectable-black")
  internal static let selectableWhite = ImageAsset(name: "selectable-white")
  internal static let selectedCheckbox = ImageAsset(name: "selected-checkbox")
  internal static let smallGradient = ImageAsset(name: "small-gradient")
  internal static let splashLogo = ImageAsset(name: "splash-logo")
  internal static let starFill = ImageAsset(name: "star-fill")
  internal static let star = ImageAsset(name: "star")
  internal static let storyPin = ImageAsset(name: "story-pin")
  internal static let successAlert = ImageAsset(name: "success-alert")
  internal static let successBlack = ImageAsset(name: "success-black")
  internal static let successWhite = ImageAsset(name: "success-white")
  internal static let termsOfUse = ImageAsset(name: "terms-of-use")
  internal static let timeBlack = ImageAsset(name: "time-black")
  internal static let time = ImageAsset(name: "time")
  internal static let trashIcon = ImageAsset(name: "trash-icon")
  internal static let turkishFlag = ImageAsset(name: "turkish-flag")
  internal static let unchecked = ImageAsset(name: "unchecked")
  internal static let unselectedRadioIcon = ImageAsset(name: "unselected-radio-icon")
  internal static let userCheck = ImageAsset(name: "user-check")
  internal static let userCross = ImageAsset(name: "user-cross")
  internal static let userPlus = ImageAsset(name: "user-plus")
  internal static let welcomeBanner = ImageAsset(name: "welcome-banner")
  internal static let wellcome = ImageAsset(name: "wellcome")
  internal static let whitePlus = ImageAsset(name: "white-plus")
}
// swiftlint:enable identifier_name line_length nesting type_body_length type_name

// MARK: - Implementation Details

internal final class ColorAsset {
  internal fileprivate(set) var name: String

  #if os(macOS)
  internal typealias Color = NSColor
  #elseif os(iOS) || os(tvOS) || os(watchOS)
  internal typealias Color = UIColor
  #endif

  @available(iOS 11.0, tvOS 11.0, watchOS 4.0, macOS 10.13, *)
  internal private(set) lazy var color: Color = {
    guard let color = Color(asset: self) else {
      fatalError("Unable to load color asset named \(name).")
    }
    return color
  }()

  #if os(iOS) || os(tvOS)
  @available(iOS 11.0, tvOS 11.0, *)
  internal func color(compatibleWith traitCollection: UITraitCollection) -> Color {
    let bundle = BundleToken.bundle
    guard let color = Color(named: name, in: bundle, compatibleWith: traitCollection) else {
      fatalError("Unable to load color asset named \(name).")
    }
    return color
  }
  #endif

  #if canImport(SwiftUI)
  @available(iOS 13.0, tvOS 13.0, watchOS 6.0, macOS 10.15, *)
  internal private(set) lazy var swiftUIColor: SwiftUI.Color = {
    SwiftUI.Color(asset: self)
  }()
  #endif

  fileprivate init(name: String) {
    self.name = name
  }
}

internal extension ColorAsset.Color {
  @available(iOS 11.0, tvOS 11.0, watchOS 4.0, macOS 10.13, *)
  convenience init?(asset: ColorAsset) {
    let bundle = BundleToken.bundle
    #if os(iOS) || os(tvOS)
    self.init(named: asset.name, in: bundle, compatibleWith: nil)
    #elseif os(macOS)
    self.init(named: NSColor.Name(asset.name), bundle: bundle)
    #elseif os(watchOS)
    self.init(named: asset.name)
    #endif
  }
}

#if canImport(SwiftUI)
@available(iOS 13.0, tvOS 13.0, watchOS 6.0, macOS 10.15, *)
internal extension SwiftUI.Color {
  init(asset: ColorAsset) {
    let bundle = BundleToken.bundle
    self.init(asset.name, bundle: bundle)
  }
}
#endif

internal struct ImageAsset {
  internal fileprivate(set) var name: String

  #if os(macOS)
  internal typealias Image = NSImage
  #elseif os(iOS) || os(tvOS) || os(watchOS)
  internal typealias Image = UIImage
  #endif

  @available(iOS 8.0, tvOS 9.0, watchOS 2.0, macOS 10.7, *)
  internal var image: Image {
    let bundle = BundleToken.bundle
    #if os(iOS) || os(tvOS)
    let image = Image(named: name, in: bundle, compatibleWith: nil)
    #elseif os(macOS)
    let name = NSImage.Name(self.name)
    let image = (bundle == .main) ? NSImage(named: name) : bundle.image(forResource: name)
    #elseif os(watchOS)
    let image = Image(named: name)
    #endif
    guard let result = image else {
      fatalError("Unable to load image asset named \(name).")
    }
    return result
  }

  #if os(iOS) || os(tvOS)
  @available(iOS 8.0, tvOS 9.0, *)
  internal func image(compatibleWith traitCollection: UITraitCollection) -> Image {
    let bundle = BundleToken.bundle
    guard let result = Image(named: name, in: bundle, compatibleWith: traitCollection) else {
      fatalError("Unable to load image asset named \(name).")
    }
    return result
  }
  #endif

  #if canImport(SwiftUI)
  @available(iOS 13.0, tvOS 13.0, watchOS 6.0, macOS 10.15, *)
  internal var swiftUIImage: SwiftUI.Image {
    SwiftUI.Image(asset: self)
  }
  #endif
}

internal extension ImageAsset.Image {
  @available(iOS 8.0, tvOS 9.0, watchOS 2.0, *)
  @available(macOS, deprecated,
    message: "This initializer is unsafe on macOS, please use the ImageAsset.image property")
  convenience init?(asset: ImageAsset) {
    #if os(iOS) || os(tvOS)
    let bundle = BundleToken.bundle
    self.init(named: asset.name, in: bundle, compatibleWith: nil)
    #elseif os(macOS)
    self.init(named: NSImage.Name(asset.name))
    #elseif os(watchOS)
    self.init(named: asset.name)
    #endif
  }
}

#if canImport(SwiftUI)
@available(iOS 13.0, tvOS 13.0, watchOS 6.0, macOS 10.15, *)
internal extension SwiftUI.Image {
  init(asset: ImageAsset) {
    let bundle = BundleToken.bundle
    self.init(asset.name, bundle: bundle)
  }

  init(asset: ImageAsset, label: Text) {
    let bundle = BundleToken.bundle
    self.init(asset.name, bundle: bundle, label: label)
  }

  init(decorative asset: ImageAsset) {
    let bundle = BundleToken.bundle
    self.init(decorative: asset.name, bundle: bundle)
  }
}
#endif

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
