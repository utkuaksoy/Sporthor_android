//
//  LocationTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 8.05.2025.
//

import UIKit
import ComponentKit
import MapKit

final class LocationTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var locationHeaderLabel: CKLabel = {
        let label = CKLabel(textColor: .white,
                            font: .bold03Compact)
        return label
    }()
    
    private lazy var locationDescriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentWeak100.color,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var seperatorView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        view.heightAnchor.constraint(equalToConstant: 1).isActive = true
        return view
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .vertical, spacing: 8)
        stackView.addArrangedSubviews([locationHeaderLabel, locationDescriptionLabel, seperatorView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
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
        contentView.addSubview(stackView)
        
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(with model: MKMapItem, isDarkTheme: Bool) {
        locationHeaderLabel.text = model.name
        locationHeaderLabel.textColor = isDarkTheme ? .white : DesignKitColorName.contentStrong900.color
        let placemark = model.placemark
        let address = [placemark.subThoroughfare,
                       placemark.thoroughfare,
                       placemark.locality,
                       placemark.administrativeArea,
                       placemark.country]
            .compactMap { $0 }
            .joined(separator: ", ")
        locationDescriptionLabel.text = address
        locationDescriptionLabel.textColor = isDarkTheme ? DesignKitColorName.contentWeak100.color : DesignKitColorName.contentSub800.color
        seperatorView.backgroundColor = isDarkTheme ? .white : DesignKitColorName.borderSoft200.color
    }
}
