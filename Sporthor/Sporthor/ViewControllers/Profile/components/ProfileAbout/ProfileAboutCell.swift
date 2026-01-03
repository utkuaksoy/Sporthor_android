//
//  ProfileAboutCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

protocol ProfileAboutCellDelegate: AnyObject {
    
}

final class ProfileAboutCell: UICollectionViewCell, ReusableView, ComponentDisplayer, ComponentDisplayerViewModelConfigurable {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = .bold03Compact
        label.textColor = DesignKitColorName.contentStrong900.color
        return label
    }()
    
    private lazy var descriptionLabel: UILabel = {
        let label = UILabel()
        label.font = .body04Compact
        label.textColor = DesignKitColorName.contentSub800.color
        label.numberOfLines = 0
        return label
    }()
    
    private lazy var detailsStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 8
        stackView.distribution = .fill
        stackView.alignment = .fill
        return stackView
    }()
    
    private lazy var mainStack: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [
            titleLabel,
            descriptionLabel,
            detailsStackView
        ])
        stackView.axis = .vertical
        stackView.spacing = 12
        stackView.alignment = .fill
        stackView.distribution = .fill
        stackView.setCustomSpacing(8, after: titleLabel)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Private Properties
    
    private var viewModel: ProfileAboutComponentViewModel?
    private weak var delegate: ProfileAboutCellDelegate?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func configure(
        with viewModel: ProfileAboutComponentViewModel,
        at indexPath: IndexPath,
        delegate: ProfileAboutCellDelegate?
    ) {
        configureTitleLabel(with: viewModel.title)
        configureDescriptionLabel(with: viewModel.aboutDescription)
        configureDetailView(with: viewModel.details)
    }
    
    private func configureTitleLabel(with title: String?) {
        titleLabel.text = title
    }
    
    private func configureDescriptionLabel(with description: String?) {
        descriptionLabel.text = description
    }
    
    private func configureDetailView(with items: [KeyValueItem]?) {
        guard let items, !items.isEmpty else {
            detailsStackView.isHidden = true
            return
        }
        detailsStackView.arrangedSubviews.forEach { $0.removeFromSuperview() }
        for item in items {
            let detailView = createDetailView(
                iconName: item.icon,
                key: item.key,
                value: item.value
            )
            detailsStackView.addArrangedSubview(detailView)
        }
    }
    
    private func createDetailView(iconName: String, key: String, value: String) -> UIStackView {
        let iconImageView = UIImageView()
        iconImageView.tintColor = .black
        iconImageView.contentMode = .scaleAspectFit
        iconImageView.setImage(with: iconName)
        
        let textLabel = UILabel()
        textLabel.text = "\(key): \(value)"
        textLabel.font = .body04Compact
        textLabel.textColor = DesignKitColorName.contentSub800.color
        
        let stackView = UIStackView(arrangedSubviews: [iconImageView, textLabel])
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.alignment = .center
        
        iconImageView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            stackView.heightAnchor.constraint(equalToConstant: 18),
            iconImageView.widthAnchor.constraint(equalToConstant: 18),
            iconImageView.heightAnchor.constraint(equalToConstant: 18)
        ])
        
        return stackView
    }
}

private extension ProfileAboutCell {
    
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(mainStack)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            mainStack.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 16),
            mainStack.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -16),
            mainStack.topAnchor.constraint(equalTo: containerView.topAnchor),
            mainStack.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
        ])
    }
}
