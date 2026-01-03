//
//  ButtonCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke.
//

import UIKit
import ComponentKit

protocol ButtonCollectionViewDelegate: AnyObject {
    func didTappedButton(tag: Int, indexPath: IndexPath?)
}

extension ButtonCollectionViewDelegate {
    func didTappedButton(tag: Int, indexPath: IndexPath?) {}
}

final class ButtonCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var button: CKButton = {
        let button = CKButton(delegate: self)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: ButtonCollectionViewDelegate?
    private var indexPath: IndexPath?
    
    // MARK: - Initialize
    override init(frame: CGRect) {
        super.init(frame: frame)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    // MARK: - Custom Methods
    private func prepareUI() {
        contentView.addSubview(button)
        
        NSLayoutConstraint.activate([
            button.topAnchor.constraint(equalTo: contentView.topAnchor),
            button.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 24),
            button.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -24),
            button.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    func bind(delegate: ButtonCollectionViewDelegate? = nil,
              title: String = "",
              titleColor: UIColor = .clear,
              backgroundColor: UIColor = .clear,
              cornerRadius: CGFloat = 0,
              borderWidth: CGFloat = 0,
              borderColor: UIColor = .clear,
              disabledTextColor: UIColor = .clear,
              disabledBackgroundColor: UIColor = .clear,
              disabledBorderColor: UIColor = .clear,
              alignment: UIControl.ContentHorizontalAlignment = .center,
              font: UIFont? = .bold03Compact,
              isEnabled: Bool = true,
              image: UIImage? = nil,
              imageTitleSpacing: CGFloat = 0,
              isUnderLine: Bool = false,
              tag: Int = 0,
              indexPath: IndexPath? = nil) {
        self.delegate = delegate
        self.indexPath = indexPath
        button.setTitle(title)
        button.setTitleColor(titleColor)
        button.setAlignment(alignment)
        button.setBackgroundColor(backgroundColor)
        button.setCornerRadius(cornerRadius)
        button.setBorderWidth(borderWidth)
        button.setBorderColor(borderColor)
        button.setDisabledTextColor(disabledTextColor)
        button.setDisabledBackgroundColor(disabledBackgroundColor)
        button.setDisabledBorderColor(disabledBorderColor)
        button.setFont(font)
        button.setEnabled(isEnabled)
        button.setImage(image)
        button.setImageTitleSpacing(imageTitleSpacing)
        button.setUnderLine(isUnderLine)
        button.tag = tag
    }
    
}

extension ButtonCollectionViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTappedButton(tag: tag, indexPath: indexPath)
    }
}
