//
//  WrappingStackView.swift
//  ComponentKit
//
//  Created by derTurke on 28.06.2025.
//


import UIKit

public final class CKWrappingStackView: UIView {
    public var spacing: CGFloat = 8
    public var lineSpacing: CGFloat = 8

    public private(set) var arrangedSubviews: [UIView] = []

    public func addArrangedSubview(_ view: UIView) {
        arrangedSubviews.append(view)
        addSubview(view)
        setNeedsLayout()
        invalidateIntrinsicContentSize()
    }

    public func addArrangedSubviews(_ views: [UIView]) {
        views.forEach { addArrangedSubview($0) }
    }

    public func removeArrangeSubview(_ view: UIView) {
        if let index = arrangedSubviews.firstIndex(of: view) {
            arrangedSubviews.remove(at: index)
            view.removeFromSuperview()
            setNeedsLayout()
            invalidateIntrinsicContentSize()
        }
    }

    public override func layoutSubviews() {
        super.layoutSubviews()

        var x: CGFloat = 0
        var y: CGFloat = 0
        var maxHeight: CGFloat = 0
        let maxWidth = bounds.width

        for subview in arrangedSubviews {
            let size = subview.intrinsicContentSize
            if x + size.width > maxWidth {
                x = 0
                y += maxHeight + lineSpacing
                maxHeight = 0
            }

            subview.frame = CGRect(x: x, y: y, width: size.width, height: size.height)
            x += size.width + spacing
            maxHeight = max(maxHeight, size.height)
        }
    }

    public override var intrinsicContentSize: CGSize {
        // layoutIfNeeded() kaldırıldı
        var x: CGFloat = 0
        var y: CGFloat = 0
        var maxHeight: CGFloat = 0
        let maxWidth = bounds.width > 0 ? bounds.width : UIScreen.main.bounds.width // fallback

        for subview in arrangedSubviews {
            let size = subview.intrinsicContentSize
            if x + size.width > maxWidth {
                x = 0
                y += maxHeight + lineSpacing
                maxHeight = 0
            }

            x += size.width + spacing
            maxHeight = max(maxHeight, size.height)
        }

        return CGSize(width: maxWidth, height: y + maxHeight)
    }
}
