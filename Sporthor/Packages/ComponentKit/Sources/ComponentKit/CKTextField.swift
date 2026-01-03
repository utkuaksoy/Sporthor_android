//
//  CKTextField.swift
//  ComponentKit
//
//  Created by derTurke on 2.02.2025.
//

import UIKit
import DesignKit

public class CKTextField: UITextField {
    // MARK: - Members
    private var borderColor: UIColor
    private var selectedBorderColor: UIColor
    private var padding: CGFloat { didSet { setNeedsDisplay() } }
    private var maxLength: Int?
    private var imagePosition: CKTextFieldImagePosition
    private let imageWidth: CGFloat
    private let imageHeight: CGFloat
    private var placeholderColor: UIColor
    
    public weak var ckDelegate: CKTextFieldDelegate?
    
    // MARK: - Initializers
    public init(delegate: CKTextFieldDelegate? = nil,
                text: String = "",
                textColor: UIColor = ColorName.contentStrong900.color,
                placeholder: String = "",
                placeholderColor: UIColor = ColorName.contentSoft600.color,
                backgroundColor: UIColor = ColorName.backgroundWeak100.color,
                cornerRadius: CGFloat = 8,
                borderWidth: CGFloat = 1,
                borderColor: UIColor = .clear,
                selectedBorderColor: UIColor = ColorName.borderStrong900.color,
                padding: CGFloat = 16,
                textAlignment: NSTextAlignment = .natural,
                font: UIFont? = UIFont.body03Compact,
                image: UIImage? = nil,
                imagePosition: CKTextFieldImagePosition = .right,
                imageWidth: CGFloat = 0,
                imageHeight: CGFloat = 0,
                isEnabled: Bool = true,
                keyboardType: UIKeyboardType = .default,
                capitalizationType: UITextAutocapitalizationType = .sentences,
                isSecureText: Bool = false,
                maxLength: Int? = nil,
                tag: Int = 0) {
        self.ckDelegate = delegate
        self.placeholderColor = placeholderColor
        self.borderColor = borderColor
        self.selectedBorderColor = selectedBorderColor
        self.padding = padding
        self.imagePosition = imagePosition
        self.imageWidth = imageWidth
        self.imageHeight = imageHeight
        self.maxLength = maxLength
        super.init(frame: .zero)
        self.borderStyle = .none
        self.text = text
        self.textColor = textColor
        self.tintColor = textColor
        self.updatePlaceholder(placeholder: placeholder)
        self.backgroundColor = backgroundColor
        self.layer.cornerRadius = cornerRadius
        self.layer.borderColor = borderColor.cgColor
        self.layer.borderWidth = borderWidth
        self.textAlignment = textAlignment
        self.font = font
        self.updateImage(image: image, imageWidth: imageWidth, imageHeight: imageHeight)
        self.isUserInteractionEnabled = isEnabled
        self.keyboardType = keyboardType
        self.autocapitalizationType = capitalizationType
        self.isSecureTextEntry = isSecureText
        self.tag = tag
        self.delegate = self
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Custom Methods
    
    public override func deleteBackward() {
        if let ckDelegate, text?.isEmpty ?? true {
            ckDelegate.textFieldDidPressBackspace(in: self)
        }
        super.deleteBackward()
    }
    
    public override func textRect(forBounds bounds: CGRect) -> CGRect {
        if imagePosition == .right {
            return bounds.insetBy(dx: padding + (self.rightView?.bounds.width ?? 0),
                                  dy: 0)
            .offsetBy(dx: -(self.rightView?.bounds.width ?? 0),
                      dy: 0)
        } else {
            return bounds.insetBy(dx: padding + (self.leftView?.bounds.width ?? 0) + (padding / 2),
                                  dy: 0)
        }
    }
    
    public override func editingRect(forBounds bounds: CGRect) -> CGRect {
        if imagePosition == .right {
            return bounds.insetBy(dx: padding + (self.rightView?.bounds.width ?? 0),
                                  dy: 0)
            .offsetBy(dx: -(self.rightView?.bounds.width ?? 0),
                      dy: 0)
        } else {
            return bounds.insetBy(dx: padding + (self.leftView?.bounds.width ?? 0) + (padding / 2),
                                  dy: 0)
        }
    }
    
    public override func rightViewRect(forBounds bounds: CGRect) -> CGRect {
        return CGRect(x: bounds.size.width - (self.rightView?.frame.size.width ?? 0) - padding,
                      y: (bounds.size.height - (self.rightView?.frame.size.height ?? 0)) / 2,
                      width: self.rightView?.frame.size.width ?? 0,
                      height: self.rightView?.frame.size.height ?? 0)
    }
    
    public override func leftViewRect(forBounds bounds: CGRect) -> CGRect {
        return CGRect(x: padding,
                      y: (bounds.size.height - (self.leftView?.frame.size.height ?? 0)) / 2,
                      width: self.leftView?.frame.size.width ?? 0,
                      height: self.leftView?.frame.size.height ?? 0)
    }
    
    public func updateImage(image: UIImage?,
                            imageWidth: CGFloat,
                            imageHeight: CGFloat) {
        if let image = image {
            let imageView = UIImageView(image: image)
            imageView.contentMode = .scaleAspectFit
            imageView.frame = CGRect(x: 0, y: 0, width: imageWidth, height: imageHeight)
            imageView.isUserInteractionEnabled = true
            imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(imageTapped)))
            switch imagePosition {
            case .left:
                self.leftView = imageView
                self.leftViewMode = .always
            case .right:
                self.rightView = imageView
                self.rightViewMode = .always
            }
        } else {
            self.leftView = nil
            self.leftViewMode = .never
            self.rightView = nil
            self.rightViewMode = .never
        }
    }
    
    @objc private func imageTapped() {
        ckDelegate?.textFieldImageTapped(self)
    }
    
    public func updatePlaceholder(placeholder: String = "") {
        if !placeholder.isEmpty {
            let placeholderText = NSAttributedString(
                string: placeholder,
                attributes: [.foregroundColor: placeholderColor]
            )
            self.attributedPlaceholder = placeholderText
        }
    }
    
    public func updatePlaceholderColor(_ color: UIColor) {
        placeholderColor = color
    }
    
    public func setSelectedBorderColor(_ color: UIColor) {
        selectedBorderColor = color
    }
    
    public func setMaxLength(_ length: Int) {
        self.maxLength = length
    }
    
    public func setImagePosition(_ position: CKTextFieldImagePosition) {
        self.imagePosition = position
    }
    
    public func setPadding(_ padding: CGFloat) {
        self.padding = padding
    }
    
    public func updateBorderColor(_ color: UIColor) {
        self.borderColor = color
        setBorderColor(color)
    }
}

extension CKTextField: UITextFieldDelegate {
    public func textFieldDidBeginEditing(_ textField: UITextField) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.layer.borderColor = self.selectedBorderColor.cgColor
        }
        
        ckDelegate?.textFieldDidBeginEditing(self)
    }
    
    public func textFieldDidEndEditing(_ textField: UITextField) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.layer.borderColor = self.borderColor.cgColor
        }
        
        ckDelegate?.textFieldDidEndEditing(self)
    }
    
    public func textFieldDidChangeSelection(_ textField: UITextField) {
        ckDelegate?.textFieldDidChangeSelection(self)
    }
    
    public func textField(
        _ textField: UITextField,
        shouldChangeCharactersIn range: NSRange,
        replacementString string: String
    ) -> Bool {
        if let maxLength = maxLength {
            let currentText = textField.text ?? ""
            let newLength = currentText.count + string.count - range.length
            if newLength > maxLength { return false }
        }
        
        if let delegate = textField.delegate,
           delegate !== self,
           let result = delegate.textField?(textField, shouldChangeCharactersIn: range, replacementString: string) {
            return result
        }
        
        return true
    }
    
    public func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        ckDelegate?.textFieldDidEndEditing(self)
        return true
    }
}
