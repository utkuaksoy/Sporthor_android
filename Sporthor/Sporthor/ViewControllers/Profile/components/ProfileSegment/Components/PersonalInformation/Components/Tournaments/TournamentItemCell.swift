//
//  TournamentItemCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class TournamentItemCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements

    private lazy var containerView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var containerStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .horizontal,
            distribution: .fill,
            alignment: .center,
            spacing: 8
        )
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([iconContainerView, contentStackView])
        return stackView
    }()
    
    private lazy var iconContainerView: UIView = {
        let view = UIView()
        view.setBorderColor(ColorName.borderSoft200.color)
        view.setBorderWidth(1)
        view.setCornerRadius(8)
        view.clipsToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .leading,
            spacing: 4
        )
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([titleLabel, stageView])
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
    
    private lazy var stageView: StageView = {
        let view = StageView()
        view.isHidden = true
        return view
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
    
    func configure(with model: TournamentModel) {
        configureTitleLabel(with: model.title)
        configureIcon(with: model.icon)
        configureStage(text: model.stage, icon: model.stageIcon)
    }

    private func configureTitleLabel(with text: String?) {
        titleLabel.text = text ?? ""
    }

    private func configureIcon(with urlString: String?) {
        guard let urlString else {
            iconImageView.isHidden = true
            return
        }
        
        iconImageView.isHidden = false
        iconImageView.setImage(
            with: urlString,
            placeholder: UIImage(named: "placeholder_tournament"),
            transition: .fade(0.2)
        )
    }

    private func configureStage(text: String?, icon: String?) {
        guard let text else {
            stageView.isHidden = true
            return
        }
        
        stageView.isHidden = false
        stageView.configure(text: text, icon: icon)
    }
}

// MARK: - Setup

private extension TournamentItemCell {
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(containerStackView)
        iconContainerView.addSubview(iconImageView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            
            iconContainerView.widthAnchor.constraint(equalToConstant: 56),
            iconContainerView.heightAnchor.constraint(equalToConstant: 56),
            
            iconImageView.topAnchor.constraint(equalTo: iconContainerView.topAnchor, constant: 6),
            iconImageView.leadingAnchor.constraint(equalTo: iconContainerView.leadingAnchor, constant: 8),
            iconImageView.trailingAnchor.constraint(equalTo: iconContainerView.trailingAnchor, constant: -8),
            iconImageView.bottomAnchor.constraint(equalTo: iconContainerView.bottomAnchor, constant: -6),
            
            stageView.heightAnchor.constraint(equalToConstant: 30)
        ])
    }
}
