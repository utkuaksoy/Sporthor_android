//
//  CKHorizontalTitleButtonView.swift
//  ComponentKit
//
//  Created by derTurke on 8.04.2025.
//

import UIKit
import DesignKit

public final class CKHorizontalTitleButtonView: UIView {
    // MARK: - UI Elements
    private lazy var label: CKLabel = {
        let label = CKLabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var button: CKButton = {
        let button = CKButton(delegate: self)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: CKHorizontalTitleButtonViewDelegate?
    
    // MARK: - Initialize
    public init(delegate: CKHorizontalTitleButtonViewDelegate? = nil,
                labelText: String = "",
                labelTextColor: UIColor = .clear,
                labelFont: UIFont? = .body04Compact,
                buttonTitle: String = "",
                buttonTitleColor: UIColor = .clear,
                buttonBackgroundColor: UIColor = .clear,
                buttonCornerRadius: CGFloat = 0,
                buttonBorderWidth: CGFloat = 0,
                buttonBorderColor: UIColor = .clear,
                buttonDisabledTextColor: UIColor = .clear,
                buttonDisabledBackgroundColor: UIColor = .clear,
                buttonDisabledBorderColor: UIColor = .clear,
                buttonFont: UIFont? = .bold03Compact,
                buttonIsEnabled: Bool = true,
                buttonImage: UIImage? = nil,
                buttonImageTitleSpacing: CGFloat = 0,
                buttonTag: Int = 0) {
        super.init(frame: .zero)
        bind(delegate: delegate,
             labelText: labelText,
             labelTextColor: labelTextColor,
             labelFont: labelFont,
             buttonTitle: buttonTitle,
             buttonTitleColor: buttonTitleColor,
             buttonBackgroundColor: buttonBackgroundColor,
             buttonCornerRadius: buttonCornerRadius,
             buttonBorderWidth: buttonBorderWidth,
             buttonBorderColor: buttonBorderColor,
             buttonDisabledTextColor: buttonDisabledTextColor,
             buttonDisabledBackgroundColor: buttonDisabledBackgroundColor,
             buttonDisabledBorderColor: buttonDisabledBorderColor,
             buttonFont: buttonFont,
             buttonIsEnabled: buttonIsEnabled,
             buttonImage: buttonImage,
             buttonImageTitleSpacing: buttonImageTitleSpacing,
             buttonTag: buttonTag)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        addSubview(label)
        addSubview(button)
        
        NSLayoutConstraint.activate([
            label.centerYAnchor.constraint(equalTo: centerYAnchor),
            label.leadingAnchor.constraint(equalTo: leadingAnchor),
            button.centerYAnchor.constraint(equalTo: label.centerYAnchor),
            button.leadingAnchor.constraint(equalTo: label.trailingAnchor, constant: 8)
        ])
    }
    
    // MARK: - Custom Methods
    
    public func bind(delegate: CKHorizontalTitleButtonViewDelegate? = nil,
                     labelText: String = "",
                     labelTextColor: UIColor = .clear,
                     labelFont: UIFont? = .body04Compact,
                     buttonTitle: String = "",
                     buttonTitleColor: UIColor = .clear,
                     buttonBackgroundColor: UIColor = .clear,
                     buttonCornerRadius: CGFloat = 0,
                     buttonBorderWidth: CGFloat = 0,
                     buttonBorderColor: UIColor = .clear,
                     buttonDisabledTextColor: UIColor = .clear,
                     buttonDisabledBackgroundColor: UIColor = .clear,
                     buttonDisabledBorderColor: UIColor = .clear,
                     buttonFont: UIFont? = .bold03Compact,
                     buttonIsEnabled: Bool = true,
                     buttonImage: UIImage? = nil,
                     buttonImageTitleSpacing: CGFloat = 0,
                     buttonTag: Int = 0) {
        self.delegate = delegate
        label.text = labelText
        label.textColor = labelTextColor
        label.font = labelFont
        button.setTitle(buttonTitle)
        button.setTitleColor(buttonTitleColor)
        button.setBackgroundColor(buttonBackgroundColor)
        button.setCornerRadius(buttonCornerRadius)
        button.setBorderWidth(buttonBorderWidth)
        button.setBorderColor(buttonBorderColor)
        button.setDisabledTextColor(buttonDisabledTextColor)
        button.setDisabledBackgroundColor(buttonDisabledBackgroundColor)
        button.setDisabledBorderColor(buttonDisabledBorderColor)
        button.setFont(buttonFont)
        button.setEnabled(buttonIsEnabled)
        button.setImage(buttonImage)
        button.setImageTitleSpacing(buttonImageTitleSpacing)
        button.tag = buttonTag
    }
}

// MARK: - CKButtonDelegate
extension CKHorizontalTitleButtonView: CKButtonDelegate {
    public func ckButtonDidTap(tag: Int) {
        delegate?.ckHorizontalButtonClicked(tag: tag)
    }
}
