//
//  CKLabel.swift
//  ComponentKit
//
//  Created by derTurke on 2.02.2025.
//

import UIKit

public final class CKLabel: UILabel {
    // MARK: - Members
    public weak var ckDelegate: CKLabelDelegate?
    
    private var padding: UIEdgeInsets = .zero
    
    // MARK: - Initializers
    public init(delegate: CKLabelDelegate? = nil,
                text: String = "",
                textColor: UIColor = .clear,
                backgroundColor: UIColor = .clear,
                cornerRadius: CGFloat = 0,
                numberOfLines: Int = 1,
                textAlignment: NSTextAlignment = .natural,
                lineBreakMode: NSLineBreakMode = .byTruncatingTail,
                font: UIFont? = .systemFont(ofSize: 14),
                isUserInteractionEnabled: Bool = false,
                tag: Int = 0,
                padding: UIEdgeInsets = .zero) {
        self.ckDelegate = delegate
        super.init(frame: .zero)
        self.text = text
        self.textColor = textColor
        self.backgroundColor = backgroundColor
        self.layer.cornerRadius = cornerRadius
        self.layer.masksToBounds = cornerRadius > 0
        self.numberOfLines = numberOfLines
        self.textAlignment = textAlignment
        self.lineBreakMode = lineBreakMode
        self.font = font
        self.isUserInteractionEnabled = isUserInteractionEnabled
        self.tag = tag
        self.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTapLabel)))
        self.padding = padding
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    @objc private func didTapLabel() {
        ckDelegate?.didTapCKLabel(tag: self.tag)
    }
    
    override public func drawText(in rect: CGRect) {
        let insetRect = rect.inset(by: padding)
        super.drawText(in: insetRect)
    }
    
    override public var intrinsicContentSize: CGSize {
        let contentSize = super.intrinsicContentSize
        return CGSize(
            width: contentSize.width + padding.left + padding.right,
            height: contentSize.height + padding.top + padding.bottom
        )
    }
}
