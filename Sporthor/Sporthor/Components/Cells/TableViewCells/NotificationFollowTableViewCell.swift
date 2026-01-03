//
//  NotificationFollowTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//

import UIKit
import ComponentKit

final class NotificationFollowTableViewCell: UITableViewCell {
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        imageView.layer.cornerRadius = 20
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 40).isActive = true
        return imageView
    }()
    
    private lazy var label: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact,
                            isUserInteractionEnabled: true)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var acceptButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Kabul Et",
                              titleColor: .white,
                              buttonBackgroundColor: DesignKitColorName.contentStrong900.color,
                              cornerRadius: 15,
                              font: .bold04Compact,
                              tag: 0)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 30).isActive = true
        return button
    }()
    
    private lazy var rejectButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Reddet",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: .white,
                              cornerRadius: 15,
                              borderWidth: 1,
                              borderColor: DesignKitColorName.contentStrong900.color,
                              font: .bold04Compact,
                              tag: 1)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 30).isActive = true
        return button
    }()
    
    // MARK: - Members
    private var isFollowingYou: Bool = false
    private var isYouFollowing: Bool = false
    
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
        selectionStyle = .none
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        contentView.addSubview(profileImageView)
        contentView.addSubview(label)
        contentView.addSubview(acceptButton)
        contentView.addSubview(rejectButton)
        
        NSLayoutConstraint.activate([
            profileImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            profileImageView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            
            label.leadingAnchor.constraint(equalTo: profileImageView.trailingAnchor, constant: 8),
            label.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            label.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            
            acceptButton.topAnchor.constraint(equalTo: label.bottomAnchor, constant: 14),
            acceptButton.leadingAnchor.constraint(equalTo: label.leadingAnchor),
            
            rejectButton.topAnchor.constraint(equalTo: acceptButton.topAnchor),
            rejectButton.leadingAnchor.constraint(equalTo: acceptButton.trailingAnchor, constant: 8)
        ])
    }
    
    // MARK: - Custom Methods
    func configure(model: NotificationModel,
                   isFollowingYou: Bool = false,
                   isYouFollowing: Bool = false) {
        
        updateButtons()
    }
    
    private func updateButtons() {
        if isFollowingYou && !isYouFollowing {
            rejectButton.isHidden = true
            acceptButton.setTitle("Sende onu takip et", for: .normal)
        } else {
            rejectButton.isHidden = false
            acceptButton.setTitle("Kabul Et", for: .normal)
        }
    }
}

extension NotificationFollowTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        switch tag {
        case 0:
            isFollowingYou = true
            isYouFollowing = false
        case 1:
            break
        default:
            break
        }
    }
}
