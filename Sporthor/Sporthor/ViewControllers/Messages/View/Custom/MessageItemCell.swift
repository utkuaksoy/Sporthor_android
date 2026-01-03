//
//  MessageItemCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//

import ComponentBaseKit
import DesignKit
import UIKit

final class MessageItemCell: UITableViewCell {
    
    // MARK: - Private UI Elements
    
    private let profileImageView: UIImageView = {
        let iv = UIImageView()
        iv.layer.cornerRadius = 24
        iv.clipsToBounds = true
        iv.contentMode = .scaleAspectFill
        iv.translatesAutoresizingMaskIntoConstraints = false
        iv.widthAnchor.constraint(equalToConstant: 48).isActive = true
        iv.heightAnchor.constraint(equalToConstant: 48).isActive = true
        return iv
    }()
    
    private let nameLabel: UILabel = {
        let label = UILabel()
        label.font = .bold04Compact
        label.textColor = ColorName.contentStrong900.color
        label.numberOfLines = 2
        return label
    }()
    
    private let messageLabel: UILabel = {
        let label = UILabel()
        label.font = .body04Compact
        label.textColor = ColorName.contentSoft600.color
        label.numberOfLines = 2
        return label
    }()
    
//    private let timeLabel: UILabel = {
//        let label = UILabel()
//        label.font = .body04Compact
//        label.textColor = ColorName.contentSoft600.color
//        label.textAlignment = .right
//        return label
//    }()
    
    private let unreadBadgeLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .systemGreen
        label.font = .bold06Compact
        label.textColor = ColorName.contentStrong900.color
        label.textAlignment = .center
        label.layer.cornerRadius = 12
        label.clipsToBounds = true
        label.isHidden = true
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let separatorView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Stack Views
    
    private lazy var textStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [nameLabel, messageLabel])
        stack.axis = .vertical
        stack.alignment = .fill
        stack.spacing = 4
        return stack
    }()
    
    private lazy var rightStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [/*timeLabel,*/unreadBadgeLabel])
        stack.axis = .vertical
        stack.alignment = .trailing
        stack.spacing = 8
        return stack
    }()
    
    private lazy var mainStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [profileImageView, textStack, rightStack])
        stack.axis = .horizontal
        stack.alignment = .center
        stack.spacing = 12
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
        
    // MARK: - Initialize

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        contentView.addSubview(mainStack)
        contentView.addSubview(separatorView)
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        contentView.addSubview(mainStack)
        contentView.addSubview(separatorView)
        setupConstraints()
    }
    
    // MARK: - Setup
    
    private func setupConstraints() {
        NSLayoutConstraint.activate([
            mainStack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            mainStack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            mainStack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            mainStack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            unreadBadgeLabel.heightAnchor.constraint(equalToConstant: 24),
            unreadBadgeLabel.widthAnchor.constraint(greaterThanOrEqualToConstant: 24),
            
            separatorView.heightAnchor.constraint(equalToConstant: 1),
            separatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            separatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            separatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Configuration
    
    func configure(item: MessagesItemModel?) {
        guard let item else { return }
        nameLabel.text = item.name
        messageLabel.text = item.lastMessage
//        timeLabel.text = item.messageDate
        profileImageView.setImage(with: item.image, placeholder: .errorUser)
        
        unreadBadgeLabel.isHidden = item.unReadMessageCount == 0
        if item.unReadMessageCount > 0 {
            unreadBadgeLabel.text = "\(item.unReadMessageCount)"
        }
    }
    
    func hideUnreadBadge() {
        unreadBadgeLabel.isHidden = true
    }
}
