//
//  UserAndButtonTableViewCell.swift
//  Sporthor
//
//  Created by GÜRHAN YUVARLAK on 29.10.2025.
//

import UIKit
import ComponentKit

protocol UserAndButtonTableViewCellDelegate: AnyObject {
    func didTappedAddButtonUserAndButtonTableViewCell(_ tag: Int)
    func didTappedFollowingFriendImageUserAndButtonTableViewCell(_ tag: Int)
}

extension UserAndButtonTableViewCellDelegate {
    func didTappedAddButtonUserAndButtonTableViewCell(_ tag: Int) {}
    func didTappedFollowingFriendImageUserAndButtonTableViewCell(_ tag: Int) {}
}

final class UserAndButtonTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.widthAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.setCornerRadius(24)
        return imageView
    }()
    
    private lazy var nameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        return label
    }()
    
    private lazy var roleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSoft600.color,
                            numberOfLines: 0,
                            font: .body06Compact)
        return label
    }()
    
    private lazy var nameAndRoleStackView: CKStackView = {
        let stackView = CKStackView(spacing: 2)
        stackView.addArrangedSubviews([nameLabel, roleLabel])
        return stackView
    }()
    
    private lazy var spacerView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }()
    
    private lazy var followingFriendImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.isUserInteractionEnabled = true
        imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(followingFriendImageTapped)))
        imageView.isHidden = true
        return imageView
    }()
    
    private lazy var addButton: CKButton = {
        let button = CKButton(delegate: self,
                              titleColor: DesignKitColorName.contentStrong900.color,
                              cornerRadius: 15,
                              borderWidth: 1,
                              borderColor: DesignKitColorName.borderStrong900.color,
                              font: .bold04Compact,
                              image: Asset.blackPlus.image,
                              imageTitleSpacing: 4)
        button.heightAnchor.constraint(equalToConstant: 30).isActive = true
        button.widthAnchor.constraint(equalToConstant: 72).isActive = true
        button.contentEdgeInsets = .init(top: 0, left: 6, bottom: 0, right: 6)
        return button
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([profileImageView, nameAndRoleStackView, spacerView, followingFriendImageView, addButton])
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 12, left: 16, bottom: 12, right: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: UserAndButtonTableViewCellDelegate?
    
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
        contentView.addSubview(contentStackView)
        
        NSLayoutConstraint.activate([
            contentStackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            contentStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            contentStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            contentStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func configure(delegate: UserAndButtonTableViewCellDelegate?,
                   image: String,
                   name: String,
                   role: String,
                   buttonTitle: String,
                   buttonImage: UIImage? = nil,
                   followingFriendImage: UIImage? = nil,
                   tag: Int = 0) {
        self.delegate = delegate
        
        profileImageView.setImage(with: image, placeholder: .errorUserImage)
        
        nameLabel.text = name
        nameLabel.isHidden = name.isEmpty
        
        roleLabel.text = role
        roleLabel.isHidden = role.isEmpty
        
        followingFriendImageView.image = followingFriendImage
        followingFriendImageView.isHidden = followingFriendImage == nil
        
        addButton.setTitle(buttonTitle)
        addButton.setImage(buttonImage)
        addButton.isHidden = buttonTitle.isEmpty
        
        self.tag = tag
    }
    
    @objc private func followingFriendImageTapped() {
        delegate?.didTappedFollowingFriendImageUserAndButtonTableViewCell(self.tag)
    }
        
}

// MARK: - CKButtonDelegate
extension UserAndButtonTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTappedAddButtonUserAndButtonTableViewCell(self.tag)
    }
}
