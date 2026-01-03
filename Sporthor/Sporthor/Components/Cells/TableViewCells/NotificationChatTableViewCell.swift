//
//  NotificationChatTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 29.06.2025.
//

import UIKit
import ComponentKit

protocol NotificationChatTableViewCellDelegate: AnyObject {
    func didTappedProfileNotificationChat(model: NotificationModel)
    func didTappedOpenChat(model: NotificationModel)
}

final class NotificationChatTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 40).isActive = true
        imageView.clipsToBounds = true
        imageView.setCornerRadius(20)
        imageView.isUserInteractionEnabled = true
        imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTappedProfile)))
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: DesignKitColorName.contentStrong900.color,
            numberOfLines: 0,
            font: .bold04Compact,
            isUserInteractionEnabled: true,
            tag: 1
        )
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(delegate: self,
                            textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact,
                            isUserInteractionEnabled: true, tag: 1)
        return label
    }()
    
    private lazy var labelStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([titleLabel, descriptionLabel])
        return stackView
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([profileImageView, labelStackView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var separatorView: CKSeparatorView = {
        let view = CKSeparatorView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Members
    private weak var delegate: NotificationChatTableViewCellDelegate?
    private var model: NotificationModel?
    
    // MARK: - Initializers
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        selectionStyle = .none
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        contentView.addSubview(stackView)
        contentView.addSubview(separatorView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            separatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            separatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            separatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func configure(delegate: NotificationChatTableViewCellDelegate? = nil,
                   model: NotificationModel) {
        self.delegate = delegate
        self.model = model
        profileImageView.setImage(with: model.data?.imageURL, placeholder: Asset.errorUserImage.image)
        titleLabel.text = "Yeni Mesaj!"
        descriptionLabel.text = (model.data?.senderName ?? "") + ", adlı kişiden yeni bir mesajınız var!"
    }
    
    @objc private func didTappedProfile() {
        guard let model else { return }
        delegate?.didTappedProfileNotificationChat(model: model)
    }
}

extension NotificationChatTableViewCell: CKLabelDelegate {
    func didTapCKLabel(tag: Int) {
        guard let model else { return }
        delegate?.didTappedOpenChat(model: model)
    }
}
