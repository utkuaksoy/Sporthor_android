//
//  TitleTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 9.04.2025.
//

import UIKit
import ComponentKit

final class TitleTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(numberOfLines: 0)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Members
    private var titleTopCons: NSLayoutConstraint!
    private var titleLeadingCons: NSLayoutConstraint!
    private var titleTrailingCons: NSLayoutConstraint!
    private var titleBottomCons: NSLayoutConstraint!
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(titleLabel)
        
        titleTopCons = titleLabel.topAnchor.constraint(equalTo: contentView.topAnchor)
        titleLeadingCons = titleLabel.leadingAnchor.constraint(equalTo: contentView.leadingAnchor)
        titleTrailingCons = titleLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor)
        titleBottomCons = titleLabel.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        
        NSLayoutConstraint.activate([
            titleTopCons,
            titleLeadingCons,
            titleTrailingCons,
            titleBottomCons
        ])
    }
    
    // MARK: - Custom Methods
    func bind(title: String,
              titleColor: UIColor = DesignKitColorName.contentStrong900.color,
              titleFont: UIFont = .bold03Compact,
              topCons: CGFloat = 16,
              leadingCons: CGFloat = 16,
              trailingCons: CGFloat = -16,
              bottomCons: CGFloat = -16) {
        titleLabel.text = title
        titleLabel.textColor = titleColor
        titleLabel.font = titleFont
        titleTopCons.constant = topCons
        titleLeadingCons.constant = leadingCons
        titleTrailingCons.constant = trailingCons
        titleBottomCons.constant = bottomCons
    }
}
