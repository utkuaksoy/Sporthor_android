/// Attention: Changes made to this file will not have any effect and will be reverted
/// when building the project. Please adjust the Stencil template `asset_extensions.stencil` instead.
/// See https://github.com/SwiftGen/SwiftGen#bundled-templates-vs-custom-ones for more information.
import UIKit


// MARK: - Images

public extension UIImage {
    static var arrowRightIcon: UIImage {
      .init(named: "arrow-right-icon", in: .module, compatibleWith: nil)!
    }
    static var chatCameraIcon: UIImage {
      .init(named: "chat-camera-icon", in: .module, compatibleWith: nil)!
    }
    static var chatLibraryIcon: UIImage {
      .init(named: "chat-library-icon", in: .module, compatibleWith: nil)!
    }
    static var chevronLeftIcon: UIImage {
      .init(named: "chevron-left-icon", in: .module, compatibleWith: nil)!
    }
    static var errorUserImage: UIImage {
      .init(named: "error-user-image", in: .module, compatibleWith: nil)!
    }
    static var errorimage: UIImage {
      .init(named: "errorimage", in: .module, compatibleWith: nil)!
    }
    static var exIcon: UIImage {
      .init(named: "ex_icon", in: .module, compatibleWith: nil)!
    }
}
