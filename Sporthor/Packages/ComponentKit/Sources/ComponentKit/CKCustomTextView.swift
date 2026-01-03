//
//  CKCustomTextView.swift
//  ComponentKit
//
//  Created by derTurke on 23.04.2025.
//

import UIKit

public final class CKCustomTextView: UITextView {
    // MARK: - UI Elements
    private lazy var placeholderLabel: CKLabel = {
        let label = CKLabel(numberOfLines: 0)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Members
    private(set) var borderColor: UIColor = .clear {
        didSet {
            layer.borderColor = borderColor.cgColor
        }
    }
    private(set) var selectedBorderColor: UIColor? = nil
    private(set) var maxLength: Int?
    private(set) var minHeight: CGFloat? = nil
    private(set) var maxHeight: CGFloat? = nil
    
    private weak var customTextViewDelegate: CKCustomTextViewDelegate? {
        didSet {
            customTextViewDelegate?.textViewPreparing(self)
        }
    }
    
    // MARK: - Initialize
    public init(customDelegate: CKCustomTextViewDelegate? = nil,
                text: String = "",
                textColor: UIColor = .clear,
                placeholder: String = "",
                placeholderColor: UIColor = .clear,
                font: UIFont = .systemFont(ofSize: 14),
                backgroundColor: UIColor = .clear,
                borderWidth: CGFloat = 0,
                borderColor: UIColor = .clear,
                selectedBorderColor: UIColor? = nil,
                cornerRadius: CGFloat = 0,
                padding: CGFloat = 0,
                maxLength: Int? = nil,
                tag: Int = 0,
                minHeight: CGFloat? = nil,
                maxHeight: CGFloat? = nil) {
        super.init(frame: .zero, textContainer: nil)
        bind(customDelegate: customDelegate,
             text: text,
             textColor: textColor,
             placeholder: placeholder,
             placeholderColor: placeholderColor,
             font: font,
             backgroundColor: backgroundColor,
             borderWidth: borderWidth,
             borderColor: borderColor,
             selectedBorderColor: selectedBorderColor,
             cornerRadius: cornerRadius,
             padding: padding,
             maxLength: maxLength,
             tag: tag,
             minHeight: minHeight,
             maxHeight: maxHeight)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        bind()
    }
    
    // MARK: - Custom Methods
    public func bind(customDelegate: CKCustomTextViewDelegate? = nil,
                     text: String = "",
                     textColor: UIColor = .clear,
                     placeholder: String = "",
                     placeholderColor: UIColor = .clear,
                     font: UIFont = .systemFont(ofSize: 14),
                     backgroundColor: UIColor = .clear,
                     borderWidth: CGFloat = 0,
                     borderColor: UIColor = .clear,
                     selectedBorderColor: UIColor? = nil,
                     cornerRadius: CGFloat = 0,
                     padding: CGFloat = 0,
                     maxLength: Int? = nil,
                     tag: Int = 0,
                     minHeight: CGFloat? = nil,
                     maxHeight: CGFloat? = nil,
                     scrollEnabled: Bool = true) {
        self.customTextViewDelegate = customDelegate
        delegate = self
        self.text = text
        self.textColor = textColor
        self.font = font
        placeholderLabel.text = placeholder
        placeholderLabel.textColor = placeholderColor
        placeholderLabel.font = font
        addSubview(placeholderLabel)
        configureConstraints(padding)
        self.backgroundColor = backgroundColor
        layer.borderWidth = borderWidth
        self.borderColor = borderColor
        if let selectedBorderColor {
            self.selectedBorderColor = selectedBorderColor
        }
        setCornerRadius(cornerRadius)
        self.maxLength = maxLength
        self.tag = tag
        updateTextContainerInset(padding)
        updatePlaceholderVisibility()
        self.minHeight = minHeight
        self.maxHeight = maxHeight
        self.isScrollEnabled = scrollEnabled
    }
    
    private func configureConstraints(_ padding: CGFloat) {
        NSLayoutConstraint.activate([
            placeholderLabel.topAnchor.constraint(equalTo: topAnchor, constant: padding),
            placeholderLabel.leadingAnchor.constraint(equalTo: leadingAnchor, constant: padding),
            placeholderLabel.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -padding),
            placeholderLabel.bottomAnchor.constraint(lessThanOrEqualTo: bottomAnchor, constant: -padding)

        ])
    }
    
    private func updateTextContainerInset(_ padding: CGFloat) {
        textContainerInset = UIEdgeInsets(top: padding,
                                          left: padding,
                                          bottom: padding,
                                          right: padding)
    }
    
    public func updatePlaceholderVisibility() {
        placeholderLabel.isHidden = !text.isEmpty
    }
    
    public override var intrinsicContentSize: CGSize {
        let fittingSize = CGSize(width: bounds.width, height: .greatestFiniteMagnitude)
        let size = self.sizeThatFits(fittingSize)
        var height = size.height
        
        if let min = minHeight {
            height = max(height, min)
        }
        
        if let max = maxHeight {
            height = min(height, max)
        }
        
        return CGSize(width: UIView.noIntrinsicMetric, height: height)
    }
}

extension CKCustomTextView: UITextViewDelegate {
    public func textViewDidBeginEditing(_ textView: UITextView) {
        if let selectedBorderColor {
            layer.borderColor = selectedBorderColor.cgColor
        }
        customTextViewDelegate?.textViewDidBeginEditing(self)
    }
    
    public func textViewDidChange(_ textView: UITextView) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.updatePlaceholderVisibility()
            
            if let maxLength {
                if maxLength > 0, self.text.count > maxLength {
                    self.text = String(self.text.prefix(maxLength))
                }
            }
            self.invalidateIntrinsicContentSize() // Hücre yüksekliği için önemli
            self.customTextViewDelegate?.textViewDidChange(self)
        }
    }
    
    public func textViewDidEndEditing(_ textView: UITextView) {
        layer.borderColor = borderColor.cgColor
        customTextViewDelegate?.textViewDidEndEditing(self)
    }
}
