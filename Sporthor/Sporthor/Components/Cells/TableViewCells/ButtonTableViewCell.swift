//
//  ButtonTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//

import UIKit
import ComponentKit

protocol ButtonTableViewCellDelegate: AnyObject {
    func didTappedButton(tag: Int, indexPath: IndexPath?)
}

extension ButtonTableViewCellDelegate {
    func didTappedButton(tag: Int, indexPath: IndexPath?) {}
}

final class ButtonTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var button: CKButton = {
        let button = CKButton(delegate: self)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: ButtonTableViewCellDelegate?
    private var indexPath: IndexPath?
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    // MARK: - Custom Methods
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(button)
        
        NSLayoutConstraint.activate([
            button.topAnchor.constraint(equalTo: contentView.topAnchor),
            button.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            button.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            button.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    func bind(delegate: ButtonTableViewCellDelegate? = nil,
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
    
    func setTitle(_ title: String) {
        button.setTitle(title)
    }
    
    func setImage(_ image: UIImage) {
        button.setImage(image)
    }
}

extension ButtonTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTappedButton(tag: tag, indexPath: indexPath)
    }
}
