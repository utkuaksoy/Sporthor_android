//
//  NotificationTrainingGroupRequestTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 30.06.2025.
//

import UIKit
import ComponentKit

protocol NotificationTrainingGroupRequestTableViewCellDelegate: AnyObject {
    func didTappedTrainingGroupRequestButton(
        _ model: NotificationModel,
        isAccepted: Bool
    )
}

extension NotificationTrainingGroupRequestTableViewCellDelegate {
    func didTappedTrainingGroupRequestButton(
        _ model: NotificationModel,
        isAccepted: Bool
    ) {}
}

final class NotificationTrainingGroupRequestTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.setCornerRadius(24)
        imageView.clipsToBounds = true
        imageView.contentMode = .scaleAspectFill
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: DesignKitColorName.contentStrong900.color,
            numberOfLines: 0,
            font: .bold04Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var messageLabel: CKLabel = {
        let label = CKLabel(
            textColor: DesignKitColorName.contentSub800.color,
            numberOfLines: 0,
            font: .body04Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var labelStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([titleLabel, messageLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var acceptButton: CKButton = {
        let button = CKButton(delegate: self,
                              titleColor: .white,
                              buttonBackgroundColor: DesignKitColorName.contentStrong900.color,
                              cornerRadius: 15,
                              tag: 1)
        button.contentEdgeInsets = UIEdgeInsets(top: 8, left: 8, bottom: 8, right: 8)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var rejectButton: CKButton = {
        let button = CKButton(delegate: self,
                              titleColor: DesignKitColorName.contentStrong900.color,
                              cornerRadius: 15,
                              borderWidth: 1,
                              borderColor: DesignKitColorName.contentStrong900.color,
                              tag: 2)
        button.contentEdgeInsets = UIEdgeInsets(top: 8, left: 8, bottom: 8, right: 8)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var buttonStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, spacing: 8)
        stackView.addArrangedSubviews([acceptButton, rejectButton])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var separatorView: CKSeparatorView = {
        let view = CKSeparatorView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Members
    private weak var delegate: NotificationTrainingGroupRequestTableViewCellDelegate?
    private var model: NotificationModel?
    
    // MARK: - Init
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupViews()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupViews()
    }

    // MARK: - Layout Setup
    private func setupViews() {
        selectionStyle = .none
        backgroundColor = .clear
        contentView.addSubview(profileImageView)
        contentView.addSubview(labelStackView)
        contentView.addSubview(buttonStackView)
        contentView.addSubview(separatorView)
        
        NSLayoutConstraint.activate([
            profileImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            profileImageView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            profileImageView.widthAnchor.constraint(equalToConstant: 48),
            profileImageView.heightAnchor.constraint(equalToConstant: 48),
            
            labelStackView.leadingAnchor.constraint(equalTo: profileImageView.trailingAnchor, constant: 12),
            labelStackView.topAnchor.constraint(equalTo: profileImageView.topAnchor),
            labelStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            
            buttonStackView.leadingAnchor.constraint(equalTo: messageLabel.leadingAnchor),
            buttonStackView.topAnchor.constraint(equalTo: messageLabel.bottomAnchor, constant: 8),
            buttonStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            separatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            separatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            separatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor)
        ])
    }
    
    // MARK: - Bind
    func configure(
        delegate: NotificationTrainingGroupRequestTableViewCellDelegate? = nil,
        model: NotificationModel,
        acceptButtonTitle: String = "Kabul Et",
        rejectButtonTitle: String = "Reddet",
        backgroundColor: UIColor = DesignKitColorName.backgroundWeak100.color
    ) {
        self.delegate = delegate
        self.model = model
        profileImageView.setImage(with: model.image, placeholder: Asset.errorUserImage.image)
        titleLabel.text = model.title
        titleLabel.isHidden = model.title.isEmpty
        messageLabel.text = model.message
        messageLabel.isHidden = model.message.isEmpty
        acceptButton.setTitle(acceptButtonTitle)
        rejectButton.setTitle(rejectButtonTitle)
        contentView.backgroundColor = backgroundColor
    }
}

extension NotificationTrainingGroupRequestTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        guard let model else { return }
        switch tag {
        case 1:
            delegate?.didTappedTrainingGroupRequestButton(model, isAccepted: true)
        case 2:
            delegate?.didTappedTrainingGroupRequestButton(model, isAccepted: false)
        default:
            break
        }
    }
}
