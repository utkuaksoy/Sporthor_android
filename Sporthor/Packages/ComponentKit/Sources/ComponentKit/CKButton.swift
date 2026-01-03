//
//  CKButton.swift
//  Sporthor
//
//  Created by derTurke on 01.02.2025.
//
//

import UIKit

public final class CKButton: UIButton {
    
    // MARK: - Members
    private var titleColor: UIColor
    private var buttonBackgroundColor: UIColor
    private var borderColor: UIColor
    private var disabledTextColor: UIColor
    private var disabledBackgroundColor: UIColor
    private var disabledBorderColor: UIColor
    private weak var ckDelegate: CKButtonDelegate?
    
    // MARK: - Initializers
    public init(delegate: CKButtonDelegate? = nil,
                title: String = "",
                titleColor: UIColor = .clear,
                buttonBackgroundColor: UIColor = .clear,
                cornerRadius: CGFloat = 0,
                borderWidth: CGFloat = 0,
                borderColor: UIColor = .clear,
                disabledTextColor: UIColor = .clear,
                disabledBackgroundColor: UIColor = .clear,
                disabledBorderColor: UIColor = .clear,
                font: UIFont? = .systemFont(ofSize: 14),
                isEnabled: Bool = true,
                image: UIImage? = nil,
                imageTitleSpacing: CGFloat = 0,
                tag: Int = 0) {
        self.ckDelegate = delegate
        self.titleColor = titleColor
        self.buttonBackgroundColor = buttonBackgroundColor
        self.borderColor = borderColor
        self.disabledTextColor = disabledTextColor
        self.disabledBackgroundColor = disabledBackgroundColor
        self.disabledBorderColor = disabledBorderColor
        
        super.init(frame: .zero)
        self.setTitle(title, for: .normal)
        self.titleLabel?.font = font
        if let image = image {
            self.setImage(image, for: .normal)
            self.imageEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: imageTitleSpacing)
            self.titleEdgeInsets = UIEdgeInsets(top: 0, left: imageTitleSpacing, bottom: 0, right: 0)
        }
        self.layer.cornerRadius = cornerRadius
        self.layer.borderWidth = borderWidth
        self.isEnabled = isEnabled
        self.tag = tag
        self.setupButton()
        self.addTarget(self, action: #selector(buttonTapped), for: .touchUpInside)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Custom Methods
    private func setupButton() {
        updateButtonAppearance()
    }
    
    private func updateButtonAppearance() {
        self.setTitleColor(self.isEnabled ? self.titleColor : self.disabledTextColor, for: .normal)
        self.backgroundColor = self.isEnabled ? self.buttonBackgroundColor : self.disabledBackgroundColor
        self.layer.borderColor = self.isEnabled ? self.borderColor.cgColor : self.disabledBorderColor.cgColor
    }
    
    @objc private func buttonTapped() {
        guard isEnabled else { return }
        ckDelegate?.ckButtonDidTap(tag: self.tag)
    }
    
    public func setEnabled(_ isEnabled: Bool) {
        self.isEnabled = isEnabled
        updateButtonAppearance()
    }
    
    public func setTitle(_ title: String) {
        self.setTitle(title, for: .normal)
    }
    
    public func setTitleColor(_ color: UIColor) {
        self.titleColor = color
        self.setTitleColor(color, for: .normal)
    }
    
    public func setBackgroundColor(_ color: UIColor) {
        self.buttonBackgroundColor = color
        self.backgroundColor = color
    }
    
    public override func setBorderColor(_ borderColor: UIColor) {
        self.borderColor = borderColor
        self.layer.borderColor = borderColor.cgColor
    }
    
    public func setFont(_ font: UIFont?) {
        self.titleLabel?.font = font
    }
    
    public func setDisabledTextColor(_ color: UIColor) {
        self.disabledTextColor = color
    }
    
    public func setDisabledBackgroundColor(_ color: UIColor) {
        self.disabledBackgroundColor = color
    }
    
    public func setDisabledBorderColor(_ color: UIColor) {
        self.disabledBorderColor = color
    }
    
    public func setAlignment(_ alignment: UIControl.ContentHorizontalAlignment) {
        self.contentHorizontalAlignment = alignment
    }
    
    public func setImage(_ image: UIImage?) {
        self.setImage(image, for: .normal)
    }
    
    public func setImageTitleSpacing(_ spacing: CGFloat) {
        self.imageEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: spacing)
        self.titleEdgeInsets = UIEdgeInsets(top: 0, left: spacing, bottom: 0, right: 0)
    }
    
    public func setTitleSpacing(horizontalSpacing: CGFloat = 0,
                                verticalSpacing: CGFloat = 0) {
        self.titleEdgeInsets = UIEdgeInsets(top: verticalSpacing,
                                            left: horizontalSpacing,
                                            bottom: verticalSpacing,
                                            right: horizontalSpacing)
    }
    
    public func setUnderLine(_ isUnderLine: Bool) {
        guard isUnderLine,
              let title = self.title(for: .normal),
              let font = self.titleLabel?.font else { return }
        
        let titleColor = self.titleColor(for: .normal) ?? .black
        
        var attributes: [NSAttributedString.Key: Any] = [
            .font: font,
            .foregroundColor: titleColor
        ]
        
        attributes[.underlineStyle] = NSUnderlineStyle.single.rawValue
        
        let attributedTitle = NSAttributedString(string: title, attributes: attributes)
        self.setAttributedTitle(attributedTitle, for: .normal)
    }
    
    public func setTag(_ tag: Int) {
        self.tag = tag
    }
}
