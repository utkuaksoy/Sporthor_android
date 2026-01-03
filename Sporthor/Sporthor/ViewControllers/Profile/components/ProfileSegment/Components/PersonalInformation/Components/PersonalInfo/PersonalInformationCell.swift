//
//  PersonalInformationCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 19.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class PersonalInformationCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.contentStrong900.color
        view.setCornerRadius(8)
        view.layer.masksToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var profileImageBackgroundView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.image = Asset.profileInfoBg.image
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var containerStackView: UIView = {
        let cKStackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .center,
            spacing: 8
        )
        cKStackView.addArrangedSubviews([profileImageView, nameLabel, detailsStackView])
        cKStackView.translatesAutoresizingMaskIntoConstraints = false
        return cKStackView
    }()
    
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.layer.cornerRadius = 48
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var nameLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentWeak200.color,
            textAlignment: .center,
            font: .bold03Compact
        )
        return label
    }()
    
    private lazy var detailsStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.distribution = .equalSpacing
        stackView.alignment = .center
        stackView.spacing = 16
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var nationalityStack = createDetailStack(title: "Uyruk")
    private lazy var birthDateStack = createDetailStack(title: "Doğum Tarihi")
    private lazy var heightStack = createDetailStack(title: "Boy")
    private lazy var weightStack = createDetailStack(title: "Kilo")
    
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
    
    func configure(with model: PersonaInfoDataModel) {
        configureImageURL(with: model.imageUrl)
        configureNameLabel(with: model.name)
        configureNationalityStack(with: model.nationalityName, flag: model.flagIcon)
        configureBirthDateStack(with: model.birthDate)
        configureHeightStack(with: model.height)
        configureWeightStack(with: model.weight)
    }
    
    // MARK: - Private Methods

    private func configureImageURL(with imageUrl: String) {
        profileImageView.setImage(with: imageUrl)
    }
    
    private func configureNameLabel(with name: String) {
        nameLabel.text = name
    }
    
    private func configureNationalityStack(with name: String?, flag: String?) {
        nationalityStack.setValue(icon: flag, text: name)
    }
    
    private func configureBirthDateStack(with birthDate: String?) {
        guard let birthDate else {
            birthDateStack.isHidden = true
            return
        }
        birthDateStack.setValue(birthDate)
    }
    
    private func configureHeightStack(with height: String?) {
        guard let height else {
            heightStack.isHidden = true
            return
        }
        heightStack.setValue(height)
    }
    
    private func configureWeightStack(with height: String?) {
        guard let height else {
            weightStack.isHidden = true
            return
        }
        weightStack.setValue(height)
    }
    
    private func createDetailStack(title: String) -> PersonalInfoItemCell {
        let stack = PersonalInfoItemCell()
        stack.setTitle(title)
        return stack
    }
}

// MARK: - Setup

private extension PersonalInformationCell {
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(profileImageBackgroundView)
        profileImageBackgroundView.addSubview(containerStackView)
        
        detailsStackView.addArrangedSubview(nationalityStack)
        detailsStackView.addArrangedSubview(birthDateStack)
        detailsStackView.addArrangedSubview(heightStack)
        detailsStackView.addArrangedSubview(weightStack)
        
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            // ContainerView Constraints
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 24),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -24),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            // Profile Image Background Constraints
            profileImageBackgroundView.topAnchor.constraint(equalTo: containerView.topAnchor),
            profileImageBackgroundView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            profileImageBackgroundView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            profileImageBackgroundView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            
            // ContainerStackView Constraints
            containerStackView.topAnchor.constraint(equalTo: profileImageBackgroundView.topAnchor, constant: 24),
            containerStackView.leadingAnchor.constraint(equalTo: profileImageBackgroundView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: profileImageBackgroundView.trailingAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: profileImageBackgroundView.bottomAnchor, constant: -24),
            
            // Profile ImageView Constraints
            profileImageView.widthAnchor.constraint(equalToConstant: 96),
            profileImageView.heightAnchor.constraint(equalToConstant: 96),
            
            // Name Label Constraints
            nameLabel.leadingAnchor.constraint(equalTo: containerStackView.leadingAnchor, constant: 8),
            nameLabel.trailingAnchor.constraint(equalTo: containerStackView.trailingAnchor, constant: -8),
            
            // DetailsStackView Constraints
            detailsStackView.leadingAnchor.constraint(equalTo: containerStackView.leadingAnchor, constant: 16),
            detailsStackView.trailingAnchor.constraint(equalTo: containerStackView.trailingAnchor, constant: -16)
        ])
    }
}
