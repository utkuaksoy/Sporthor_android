//
//  CarearHistoryCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class CareerHistoryItemCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private lazy var containerStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .fill,
            spacing: .zero
        )
        stackView.addArrangedSubviews([contentStackView, bottomSeparator])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var bottomSeparator: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .horizontal,
            distribution: .fill,
            alignment: .center,
            spacing: 10
        )
        stackView.addArrangedSubviews([teamLogoImageContainerView, titleAndDateLabelsStackView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var teamLogoImageContainerView: UIView = {
        let view = UIView()
        view.setCornerRadius(24)
        view.setBorderColor(ColorName.borderSoft200.color)
        view.setBorderWidth(1)
        view.clipsToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var teamLogoImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var titleAndDateLabelsStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .leading,
            spacing: 6
        )
        stackView.addArrangedSubviews([titleLabel, dateLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentStrong900.color,
            font: .bold04Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var dateLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentSoft600.color,
            font: .body04Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Initializer

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configure

    func configure(with model: CareerHistoryItemModel) {
        titleLabel.text = model.teamName
        
        if let startDate = model.startDate, let endDate = model.endDate {
            dateLabel.text = "\(startDate) - \(endDate)"
        }
        
        if let teamLogoURL = model.teamLogoURL {
            teamLogoImageView.setImage(with: teamLogoURL)
        } else {
            teamLogoImageView.isHidden = true
        }
    }
}

// MARK: - Setup

private extension CareerHistoryItemCell {
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(containerStackView)
        teamLogoImageContainerView.addSubview(teamLogoImageView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerStackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            teamLogoImageContainerView.widthAnchor.constraint(equalToConstant: 56),
            teamLogoImageContainerView.heightAnchor.constraint(equalToConstant: 56),
            
            teamLogoImageView.topAnchor.constraint(equalTo: teamLogoImageContainerView.topAnchor, constant: 4),
            teamLogoImageView.leadingAnchor.constraint(equalTo: teamLogoImageContainerView.leadingAnchor, constant: 4),
            teamLogoImageView.trailingAnchor.constraint(equalTo: teamLogoImageContainerView.trailingAnchor, constant: -4),
            teamLogoImageView.bottomAnchor.constraint(equalTo: teamLogoImageContainerView.bottomAnchor, constant: -4),
            
            bottomSeparator.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            bottomSeparator.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            bottomSeparator.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            bottomSeparator.heightAnchor.constraint(equalToConstant: 1)
        ])
    }
}
