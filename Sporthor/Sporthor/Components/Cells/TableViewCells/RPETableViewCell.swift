//
//  RPETableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 26.07.2025.
//

import UIKit
import ComponentKit

final class RPETableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var pointLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold03Compact)
        label.widthAnchor.constraint(equalToConstant: 40).isActive = true
        label.heightAnchor.constraint(equalToConstant: 40).isActive = true
        return label
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold03Compact)
        return label
    }()
    
    private lazy var subtitleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var titleStackView: CKStackView = {
        let stackView = CKStackView(spacing: 4)
        stackView.addArrangedSubviews([titleLabel, subtitleLabel])
        return stackView
    }()
    
    private lazy var starLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            textAlignment: .right,
                            font: .bold03Compact)
        label.widthAnchor.constraint(equalToConstant: 40).isActive = true
        label.heightAnchor.constraint(equalToConstant: 40).isActive = true
        return label
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([pointLabel, titleStackView, starLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 8, left: 16, bottom: 8, right: 16)
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
        selectionStyle = .none
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        contentView.addSubview(contentStackView)
        NSLayoutConstraint.activate([
            contentStackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            contentStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            contentStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            contentStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    func bind(_ model: RPEModel) {
        pointLabel.text = model.point
        titleLabel.text = model.title
        subtitleLabel.text = model.subtitle
        subtitleLabel.isHidden = model.subtitle.isEmpty
        starLabel.text = model.star
        starLabel.isHidden = model.star.isEmpty
    }
}
