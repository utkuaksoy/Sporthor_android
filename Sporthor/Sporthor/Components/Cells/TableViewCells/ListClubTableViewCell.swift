//
//  ListClubTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 3.07.2025.
//

import UIKit
import ComponentKit

final class ListClubTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var clubImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.layer.cornerRadius = 24
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.heightAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 48).isActive = true
        return imageView
    }()
    
    private lazy var clubNameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold02Compact,
                            isUserInteractionEnabled: false)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var clubBranchLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .bold03Compact,
                            isUserInteractionEnabled: false)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var clubFoundationYearLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSoft600.color,
                            numberOfLines: 0,
                            font: .bold04Compact,
                            isUserInteractionEnabled: false)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var clubCountyLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSoft600.color,
                            numberOfLines: 0,
                            font: .body04Compact,
                            isUserInteractionEnabled: false)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var clubAddressLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact,
                            isUserInteractionEnabled: false)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var verticalStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([clubNameLabel,
                                       clubBranchLabel,
                                       clubFoundationYearLabel,
                                       clubCountyLabel,
                                       clubAddressLabel])
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
        stackView.addArrangedSubviews([clubImageView, verticalStackView, rightIconImageView])
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
    func configure(with club: SportClub, rightIcon: UIImage = Asset.chevronRightGrey.image) {
        clubImageView.setImage(with: club.logo)
        clubNameLabel.text = club.clubName
        clubBranchLabel.text = club.branch?.name
        clubFoundationYearLabel.text = club.foundationYear
        clubCountyLabel.text = club.county
        clubAddressLabel.text = club.address
        rightIconImageView.image = rightIcon
    }
    
    
    func configureCoach(_ club: GetClubsAndDetailClub, rightIcon: UIImage = Asset.chevronRightGrey.image) {
        clubImageView.setImage(with: club.logo)
        clubNameLabel.text = club.name
        clubBranchLabel.isHidden = true
        clubFoundationYearLabel.isHidden = true
        clubCountyLabel.isHidden = false
        clubAddressLabel.isHidden = true
        rightIconImageView.image = rightIcon
    }
}
