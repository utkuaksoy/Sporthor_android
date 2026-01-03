//
//  ChatUserCell.swift
//  Sporthor
//
//  Created by Mesut on 26.03.2025.
//

import CommonKit
import ComponentKit
import UIKit
import DesignKit

protocol ChatUserCellDelegate: AnyObject {
    func followingFriendImageTappedChatUserCell(_ tag: Int)
}

extension ChatUserCellDelegate {
    func followingFriendImageTappedChatUserCell(_ tag: Int) {}
}

final class ChatUserCell: UITableViewCell {
    
    static let reuseIdentifier = "ChatUserCell"
    
    private let profileImageView: UIImageView = {
        let iv = UIImageView()
        iv.contentMode = .scaleAspectFill
        iv.layer.cornerRadius = 24
        iv.clipsToBounds = true
        iv.translatesAutoresizingMaskIntoConstraints = false
        iv.widthAnchor.constraint(equalToConstant: 48).isActive = true
        iv.heightAnchor.constraint(equalToConstant: 48).isActive = true
        return iv
    }()
    
    private let nameLabel: UILabel = {
        let label = UILabel()
        label.font = .bold04Compact
        label.textColor = ColorName.contentStrong900.color
        return label
    }()
    
    private let roleLabel: UILabel = {
        let label = UILabel()
        label.font = .interTight400
        label.textColor = ColorName.contentSoft600.color
        return label
    }()
    
    private lazy var textStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [nameLabel, roleLabel])
        stack.axis = .vertical
        stack.spacing = 2
        return stack
    }()
    
    private lazy var followingFriendImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.isHidden = true
        imageView.isUserInteractionEnabled = true
        imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(followingFriendImageTapped)))
        return imageView
    }()
    
    private let radioButton: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.image = Asset.successAlert.image
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var mainStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [profileImageView, textStack, followingFriendImageView , radioButton])
        stack.axis = .horizontal
        stack.spacing = 12
        stack.alignment = .center
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    // MARK: - Properties
    
    private var isGroupSelection: Bool = false
    private weak var delegate: ChatUserCellDelegate?
    
    // MARK: - Initialize
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Setup
    
    private func setupViews() {
        contentView.addSubview(mainStack)
        selectionStyle = .none
    }
    
    private func setupConstraints() {
        NSLayoutConstraint.activate([
            mainStack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            mainStack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            mainStack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 12),
            mainStack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -12)
        ])
    }
    
    // MARK: - Configure
    
    func configure(
        name: String,
        role: String,
        imageUrl: String,
        isGroupSelection: Bool = false,
        isSelected: Bool = false,
        followingFriendImage: UIImage? = nil,
        tag: Int = 0
    ) {
        nameLabel.text = name
        roleLabel.text = role
        profileImageView.setImage(with: imageUrl, placeholder: .errorUserImage)
        
        self.isGroupSelection = isGroupSelection
        radioButton.isHidden = !isGroupSelection
        
        if isGroupSelection {
            radioButton.image = isSelected ? Asset.success.image : Asset.unselectedRadioIcon.image
        }
        
        followingFriendImageView.isHidden = followingFriendImage == nil
        
        self.tag = tag
    }
    
    func bind(
        delegate: ChatUserCellDelegate? = nil,
        name: String,
        role: String = "",
        imageUrl: String,
        isGroupSelection: Bool = false,
        isSelected: Bool = false,
        followingFriendImage: UIImage? = nil,
        tag: Int = 0
    ) {
        self.delegate = delegate
        nameLabel.text = name
        roleLabel.text = role
        profileImageView.setImage(with: imageUrl, placeholder: .errorUserImage)
        
        self.isGroupSelection = isGroupSelection
        radioButton.isHidden = !isGroupSelection
        
        if isGroupSelection {
            radioButton.image = isSelected ? Asset.checkSuccessPrimary.image : Asset.unselectedRadioIcon.image
        }
        
        followingFriendImageView.image = followingFriendImage
        followingFriendImageView.isHidden = followingFriendImage == nil
        
        self.tag = tag
    }
    
    @objc private func followingFriendImageTapped() {
        delegate?.followingFriendImageTappedChatUserCell(self.tag)
    }
}
