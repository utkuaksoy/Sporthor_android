//
//  MatchesLeagueInfoView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.03.2025.
//

import ComponentKit
import DesignKit
import UIKit

final class MatchesLeagueInfoView: UIView {
    
    // MARK: - Private UI Elements
    
    private lazy var stackView: CKStackView = {
        let stack = CKStackView(
            axis: .horizontal,
            distribution: .fill,
            alignment: .fill,
            spacing: 8
        )
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = Asset.facebook.image
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            text: "TVF Vodafone Sultanlar Ligi",
            textColor: ColorName.contentSub800.color,
            font: .interTight500
        )
        label.setContentHuggingPriority(.required, for: .horizontal)
        label.setContentCompressionResistancePriority(.required, for: .horizontal)
        return label
    }()
    
    private lazy var separatorLabel: CKLabel = {
        let label = CKLabel(
            text: "•",
            textColor: ColorName.contentSub800.color,
            font: .interTight500
        )
        label.setContentHuggingPriority(.required, for: .horizontal)
        label.setContentCompressionResistancePriority(.required, for: .horizontal)
        return label
    }()
        
    private lazy var dateLabel: CKLabel = {
        let label = CKLabel(
            text: "18 Ocak 2025",
            textColor: ColorName.contentSub800.color,
            textAlignment: .left,
            font: .interTight500
        )
        return label
    }()
    
    // MARK: - Initializer
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configure Methods
    
    func configure(with model: LeagueInfoModel) {
        iconImageView.kf.setImage(with: URL(string: model.leagueLogoURL))
        titleLabel.text = model.title
        dateLabel.text = model.date
    }
}

// MARK: - Setup

private extension MatchesLeagueInfoView {
    func setupView() {
        addSubview(stackView)
        stackView.addArrangedSubview(iconImageView)
        stackView.addArrangedSubview(titleLabel)
        stackView.addArrangedSubview(separatorLabel)
        stackView.addArrangedSubview(dateLabel)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            iconImageView.widthAnchor.constraint(equalToConstant: 18),
            iconImageView.heightAnchor.constraint(equalToConstant: 18)
        ])
    }
}
