//
//  AddPersonInfoAndButtonTableViewCell.swift
//  Sporthor
//
//  Created by GÜRHAN YUVARLAK on 29.10.2025.
//

import UIKit
import ComponentKit

protocol AddPersonInfoAndButtonTableViewCellDelegate: AnyObject {
    func didTappedAddPersonInfoAndButtonTableViewCell(_ tag: Int)
}

final class AddPersonInfoAndButtonTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var ckAddPersonInfoAndButtonView: CKAddPersonInfoAndButtonView = {
        let view = CKAddPersonInfoAndButtonView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Members
    private weak var delegate: AddPersonInfoAndButtonTableViewCellDelegate?
    
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
        contentView.addSubview(ckAddPersonInfoAndButtonView)
        
        NSLayoutConstraint.activate([
            ckAddPersonInfoAndButtonView.topAnchor.constraint(equalTo: contentView.topAnchor),
            ckAddPersonInfoAndButtonView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            ckAddPersonInfoAndButtonView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            ckAddPersonInfoAndButtonView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: AddPersonInfoAndButtonTableViewCellDelegate? = nil,
              title: String? = nil,
              info: String? = nil,
              buttonTitle: String? = nil,
              tag: Int = 0) {
        self.delegate = delegate
        ckAddPersonInfoAndButtonView.bind(delegate: self,
                                          title: title,
                                          info: info,
                                          buttonTitle: buttonTitle,
                                          tag: tag)
    }
}

extension AddPersonInfoAndButtonTableViewCell: CKAddPersonInfoAndButtonViewDelegate {
    func didTappedCKAddPersonInfoAndButtonView(_ tag: Int) {
        delegate?.didTappedAddPersonInfoAndButtonTableViewCell(tag)
    }
}
