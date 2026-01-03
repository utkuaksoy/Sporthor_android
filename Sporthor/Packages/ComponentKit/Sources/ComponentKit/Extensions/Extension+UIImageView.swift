//
//  Extension+UIImageView.swift
//  ComponentKit
//
//  Created by derTurke on 12.03.2025.
//

import DesignKit
import Kingfisher
import UIKit

public enum ContentModeStrategy {
    case fit
    case fill
    case auto
}

public extension UIView {
    func addGradientBorder(colors: [UIColor],
                           borderWidth: CGFloat,
                           startPoint: CGPoint,
                           endPoint: CGPoint) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            let gradientLayer = CAGradientLayer()
            gradientLayer.frame = self.bounds
            gradientLayer.colors = colors.map { $0.cgColor }
            gradientLayer.startPoint = startPoint
            gradientLayer.endPoint = endPoint
            
            let shape = CAShapeLayer()
            shape.lineWidth = borderWidth
            shape.path = UIBezierPath(ovalIn: self.bounds.insetBy(dx: borderWidth / 2, dy: borderWidth / 2)).cgPath
            shape.fillColor = UIColor.clear.cgColor
            shape.strokeColor = UIColor.black.cgColor
            gradientLayer.mask = shape
            
            self.layer.addSublayer(gradientLayer)
        }
    }
    
    func removeGradientBorder() {
        layer.sublayers?.removeAll(where: { $0.name == "gradientBorder" })
    }
}

public extension UIImageView {
    @discardableResult
    func setImage(
        with urlString: String?,
        placeholder: UIImage? = nil,
        errorImage: UIImage? = nil,
        showIndicator: Bool = true,
        transition: ImageTransition = .fade(0.25)
    ) -> UIImage {
        guard let urlString = urlString,
              let url = URL(string: urlString) else {
            self.image = placeholder
            return image ?? UIImage()
        }

        if showIndicator {
            self.kf.indicatorType = .activity
        } else {
            self.kf.indicatorType = .none
        }

        self.kf.setImage(
            with: url,
            placeholder: placeholder,
            options: [
                .transition(transition),
                .cacheOriginalImage
            ]
        ) { result in
            switch result {
            case .success:
                break
            case .failure:
                self.image = errorImage ?? placeholder
                
            }
        }
        return self.image ?? UIImage()
    }
}

public extension UIImage {
    func resize(to size: CGSize) -> UIImage {
        let format = UIGraphicsImageRendererFormat.default()
        format.scale = self.scale
        return UIGraphicsImageRenderer(size: size, format: format).image { _ in
            self.draw(in: CGRect(origin: .zero, size: size))
        }
    }
    
    var isPortrait: Bool {
        return size.height > size.width
    }
    
    var isLandscape: Bool {
        return size.width > size.height
    }
    
    convenience init?(color: UIColor, size: CGSize = CGSize(width: 1, height: 1)) {
        UIGraphicsBeginImageContextWithOptions(size, false, 0)
        color.setFill()
        UIRectFill(CGRect(origin: .zero, size: size))
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        guard let cgImage = image?.cgImage else { return nil }
        self.init(cgImage: cgImage)
    }
}
