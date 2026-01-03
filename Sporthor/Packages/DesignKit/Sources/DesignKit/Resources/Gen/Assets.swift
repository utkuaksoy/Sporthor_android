// swiftlint:disable all
// Generated using SwiftGen — https://github.com/SwiftGen/SwiftGen

#if os(macOS)
  import AppKit.NSColor
  public typealias Color = NSColor
#elseif os(iOS) || os(tvOS) || os(watchOS)
  import UIKit.UIColor
  public typealias Color = UIColor
#endif

// swiftlint:disable superfluous_disable_command file_length implicit_return

// MARK: - Colors

// swiftlint:disable identifier_name line_length type_body_length
public struct ColorName {
  public let rgbaValue: UInt32
  public var color: Color { return Color(named: self) }

  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#b1fa63"></span>
  /// Alpha: 100% <br/> (0xb1fa63ff)
  public static let backgroundPrimaryGreen = ColorName(rgbaValue: 0xb1fa63ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#e4e4e4"></span>
  /// Alpha: 100% <br/> (0xe4e4e4ff)
  public static let backgroundSoft200 = ColorName(rgbaValue: 0xe4e4e4ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#d2d3d3"></span>
  /// Alpha: 100% <br/> (0xd2d3d3ff)
  public static let backgroundSub300 = ColorName(rgbaValue: 0xd2d3d3ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#333737"></span>
  /// Alpha: 100% <br/> (0x333737ff)
  public static let backgroundSurface800 = ColorName(rgbaValue: 0x333737ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#f4f4f4"></span>
  /// Alpha: 100% <br/> (0xf4f4f4ff)
  public static let backgroundWeak100 = ColorName(rgbaValue: 0xf4f4f4ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#ffffff"></span>
  /// Alpha: 100% <br/> (0xffffffff)
  public static let backgroundWhite0 = ColorName(rgbaValue: 0xffffffff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#335cff"></span>
  /// Alpha: 100% <br/> (0x335cffff)
  public static let blue500 = ColorName(rgbaValue: 0x335cffff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#294acc"></span>
  /// Alpha: 100% <br/> (0x294accff)
  public static let blue600 = ColorName(rgbaValue: 0x294accff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#e1e4e6"></span>
  /// Alpha: 100% <br/> (0xe1e4e6ff)
  public static let borderPrimary = ColorName(rgbaValue: 0xe1e4e6ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#e4e4e4"></span>
  /// Alpha: 100% <br/> (0xe4e4e4ff)
  public static let borderSoft200 = ColorName(rgbaValue: 0xe4e4e4ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#121515"></span>
  /// Alpha: 100% <br/> (0x121515ff)
  public static let borderStrong900 = ColorName(rgbaValue: 0x121515ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#d2d3d3"></span>
  /// Alpha: 100% <br/> (0xd2d3d3ff)
  public static let borderSub300 = ColorName(rgbaValue: 0xd2d3d3ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#d2d3d3"></span>
  /// Alpha: 100% <br/> (0xd2d3d3ff)
  public static let contentDisable300 = ColorName(rgbaValue: 0xd2d3d3ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#606464"></span>
  /// Alpha: 100% <br/> (0x606464ff)
  public static let contentSoft600 = ColorName(rgbaValue: 0x606464ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#121515"></span>
  /// Alpha: 100% <br/> (0x121515ff)
  public static let contentStrong900 = ColorName(rgbaValue: 0x121515ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#333737"></span>
  /// Alpha: 100% <br/> (0x333737ff)
  public static let contentSub800 = ColorName(rgbaValue: 0x333737ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#f4f4f4"></span>
  /// Alpha: 100% <br/> (0xf4f4f4ff)
  public static let contentWeak100 = ColorName(rgbaValue: 0xf4f4f4ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#e4e4e4"></span>
  /// Alpha: 100% <br/> (0xe4e4e4ff)
  public static let contentWeak200 = ColorName(rgbaValue: 0xe4e4e4ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#ffffff"></span>
  /// Alpha: 100% <br/> (0xffffffff)
  public static let contentWhite0 = ColorName(rgbaValue: 0xffffffff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#d9d9d9"></span>
  /// Alpha: 100% <br/> (0xd9d9d9ff)
  public static let d9d9d9 = ColorName(rgbaValue: 0xd9d9d9ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#ff6359"></span>
  /// Alpha: 100% <br/> (0xff6359ff)
  public static let errorBase500 = ColorName(rgbaValue: 0xff6359ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#e0fdc1"></span>
  /// Alpha: 100% <br/> (0xe0fdc1ff)
  public static let green200 = ColorName(rgbaValue: 0xe0fdc1ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#8ec84f"></span>
  /// Alpha: 100% <br/> (0x8ec84fff)
  public static let green600 = ColorName(rgbaValue: 0x8ec84fff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#606464"></span>
  /// Alpha: 100% <br/> (0x606464ff)
  public static let neutral600 = ColorName(rgbaValue: 0x606464ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#ffe5fc"></span>
  /// Alpha: 100% <br/> (0xffe5fcff)
  public static let pink100 = ColorName(rgbaValue: 0xffe5fcff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#663160"></span>
  /// Alpha: 100% <br/> (0x663160ff)
  public static let pink800 = ColorName(rgbaValue: 0x663160ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#ff7bf0"></span>
  /// Alpha: 100% <br/> (0xff7bf0ff)
  public static let primaryPink = ColorName(rgbaValue: 0xff7bf0ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#947aff"></span>
  /// Alpha: 100% <br/> (0x947affff)
  public static let primaryPurple = ColorName(rgbaValue: 0x947affff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#ff6359"></span>
  /// Alpha: 100% <br/> (0xff6359ff)
  public static let red500 = ColorName(rgbaValue: 0xff6359ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#cc4f47"></span>
  /// Alpha: 100% <br/> (0xcc4f47ff)
  public static let red600 = ColorName(rgbaValue: 0xcc4f47ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#b1fa63"></span>
  /// Alpha: 100% <br/> (0xb1fa63ff)
  public static let successBase500 = ColorName(rgbaValue: 0xb1fa63ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#476428"></span>
  /// Alpha: 100% <br/> (0x476428ff)
  public static let successDark800 = ColorName(rgbaValue: 0x476428ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#effee0"></span>
  /// Alpha: 100% <br/> (0xeffee0ff)
  public static let successLighter100 = ColorName(rgbaValue: 0xeffee0ff)
  /// <span style="display:block;width:3em;height:2em;border:1px solid black;background:#ffffff"></span>
  /// Alpha: 40% <br/> (0xffffff66)
  public static let whiteOpacity40 = ColorName(rgbaValue: 0xffffff66)
}
// swiftlint:enable identifier_name line_length type_body_length

// MARK: - Implementation Details

internal extension Color {
  convenience init(rgbaValue: UInt32) {
    let components = RGBAComponents(rgbaValue: rgbaValue).normalized
    self.init(red: components[0], green: components[1], blue: components[2], alpha: components[3])
  }
}

private struct RGBAComponents {
  let rgbaValue: UInt32

  private var shifts: [UInt32] {
    [
      rgbaValue >> 24, // red
      rgbaValue >> 16, // green
      rgbaValue >> 8,  // blue
      rgbaValue        // alpha
    ]
  }

  private var components: [CGFloat] {
    shifts.map { CGFloat($0 & 0xff) }
  }

  var normalized: [CGFloat] {
    components.map { $0 / 255.0 }
  }
}

public extension Color {
  convenience init(named color: ColorName) {
    self.init(rgbaValue: color.rgbaValue)
  }
}

