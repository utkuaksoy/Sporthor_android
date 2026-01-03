//
//  EmptyTitleAndButtonTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 18.05.2025.
//

import UIKit
import ComponentKit

protocol EmptyTitleAndButtonTableViewCellDelegate: AnyObject {
    func didTappedEmptyTitleAndButtonTableViewCell()
}

final class EmptyTitleAndButtonTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold03Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var button: CKButton = {
        let button = CKButton(delegate: self,
                              titleColor: DesignKitColorName.contentStrong900.color,
                              cornerRadius: 23,
                              borderWidth: 1,
                              borderColor: DesignKitColorName.contentStrong900.color,
                              font: .bold03Compact)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: EmptyTitleAndButtonTableViewCellDelegate?
    
    // MARK: - Initializers
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(titleLabel)
        contentView.addSubview(button)
        
        NSLayoutConstraint.activate([
            titleLabel.topAnchor.constraint(equalTo: contentView.topAnchor),
            titleLabel.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 24),
            titleLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -24),
            
            button.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 16),
            button.leadingAnchor.constraint(equalTo: titleLabel.leadingAnchor),
            button.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            button.heightAnchor.constraint(equalToConstant: 46)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: EmptyTitleAndButtonTableViewCellDelegate?,
              title: String,
              buttonTitle: String,
              buttonImage: UIImage? = nil,
              buttonImageSpacing: CGFloat = 0,
              buttonWidth: CGFloat = 0) {
        self.delegate = delegate
        self.titleLabel.text = title
        self.button.setTitle(buttonTitle)
        self.button.setImage(buttonImage)
        self.button.setImageTitleSpacing(buttonImageSpacing)
        self.button.widthAnchor.constraint(equalToConstant: buttonWidth).isActive = true
        self.layoutIfNeeded()
    }
}

extension EmptyTitleAndButtonTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTappedEmptyTitleAndButtonTableViewCell()
    }
}
