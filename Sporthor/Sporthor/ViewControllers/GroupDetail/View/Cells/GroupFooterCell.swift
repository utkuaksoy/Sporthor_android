//
//  GroupFooterCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import UIKit
import ComponentBaseKit
import ComponentKit
import DesignKit

final class GroupFooterCell: UICollectionViewCell, ReusableView {
    enum ButtonType {
        case media
        case leave
        
        var image: UIImage {
            switch self {
            case .media:
                return Asset.mediaImage.image
            case .leave:
                return Asset.leaveChatIcon.image
            }
        }
        
        var textColor: UIColor {
            switch self {
            case .media:
                return ColorName.contentStrong900.color
            case .leave:
                return ColorName.errorBase500.color
            }
        }
    }
    
    // MARK: - Private UI Elements

    private lazy var button: UIButton = {
        let button = UIButton(type: .system)
        button.titleLabel?.font = .bold04Compact
        button.contentHorizontalAlignment = .left
        button.addTarget(self, action: #selector(buttonTapped(_:)), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        let spacing: CGFloat = 8
        button.imageEdgeInsets = UIEdgeInsets(top: 0, left: -spacing/2, bottom: 0, right: spacing/2)
        button.titleEdgeInsets = UIEdgeInsets(top: 0, left: spacing/2, bottom: 0, right: -spacing/2)
        return button
    }()
    
    private lazy var countLabel: UILabel = {
        let label = UILabel()
        label.font = .body04Compact
        label.textColor = ColorName.contentSoft600.color
        label.textAlignment = .right
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Properties
    var buttonTapped: (() -> Void)?
    
    // MARK: - Initialization
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configuration

    func configure(title: String, count: Int? = nil, type: ButtonType) {
        button.setTitle(title, for: .normal)
        button.setImage(type.image, for: .normal)
        button.setTitleColor(type.textColor, for: .normal)
        button.tintColor = type.textColor
        
        if let count = count {
            countLabel.text = "\(count)"
            countLabel.isHidden = false
        } else {
            countLabel.isHidden = true
        }
    }
    
    // MARK: - Actions
    @objc private func buttonTapped(_ sender: UIButton) {
        buttonTapped?()
    }
}

// MARK: - Setup
private extension GroupFooterCell {
    func setupViews() {
        contentView.backgroundColor = .white
        contentView.addSubview(button)
        contentView.addSubview(countLabel)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            button.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            button.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            button.heightAnchor.constraint(equalToConstant: 44),
            
            countLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            countLabel.centerYAnchor.constraint(equalTo: contentView.centerYAnchor)
        ])
    }
} 
