//
//  TrainingGroupTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 1.07.2025.
//

import UIKit
import ComponentKit

final class TrainingGroupTableViewCell: UITableViewCell {
    
    // MARK: - UI Elements
    private lazy var groupImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.setCornerRadius(24)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.heightAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 48).isActive = true
        return imageView
    }()
    
    private lazy var groupNameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold03Compact,
                            isUserInteractionEnabled: false)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var groupTeamLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .bold04Compact,
                            isUserInteractionEnabled: false)
        label.isHidden = true
        return label
    }()
    
    private lazy var groupDescriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact,
                            isUserInteractionEnabled: false)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var verticalStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([groupNameLabel,
                                       groupTeamLabel,
                                       groupDescriptionLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var rightIconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = Asset.chevronRightGrey.image
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var horizontalStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    alignment: .center,
                                    spacing: 12,
                                    cornerRadius: 8,
                                    borderWidth: 1,
                                    borderColor: DesignKitColorName.borderSoft200.color)
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = UIEdgeInsets(top: 8, left: 8, bottom: 8, right: 8)
        stackView.addArrangedSubviews([groupImageView, verticalStackView, rightIconImageView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
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
        contentView.addSubview(horizontalStackView)
        
        NSLayoutConstraint.activate([
            horizontalStackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 8),
            horizontalStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            horizontalStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            horizontalStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -8)
        ])
    }
    
    // MARK: - Configuration
    func configure(with group: GetTrainingGroupUserModel,
                   rightImage: UIImage = Asset.chevronRightGrey.image) {
        groupImageView.setImage(with: group.team?.detail)
        groupNameLabel.text = group.groupName
        groupTeamLabel.text = group.team?.name ?? ""
        groupTeamLabel.isHidden = group.team?.name?.isEmpty ?? true
        groupDescriptionLabel.text = "\(group.users.count) Sporcu - \(group.coaches.count) Antrenör"
        rightIconImageView.image = rightImage
    }
    
    func configureWithCoaches(_ model: GetClubsAndDetailTrainingGroup,
                              image: String) {
        groupImageView.isHidden = image.isEmpty
        groupImageView.setImage(with: image)
        groupNameLabel.font = .bold04Compact
        groupNameLabel.text = model.name
    }
}
