//
//  Extension+UITableView.swift
//  Sporthor
//
//  Created by derTurke on 25.02.2025.
//

import UIKit

extension UITableView {
    func removeEmptyCell() {
        let view = UIView()
        view.backgroundColor = .clear
        tableFooterView = view
        sectionFooterHeight = 0
    }
}

extension UITableViewCell {
    static func dequeueNib(from tableView: UITableView, at indexPath: IndexPath) -> Self {
        let identifier = String(describing: self)

        if tableView.dequeueReusableCell(withIdentifier: identifier) == nil {
            tableView.register(UINib(nibName: identifier, bundle: nil), forCellReuseIdentifier: identifier)
        }

        guard let cell = tableView.dequeueReusableCell(withIdentifier: identifier, for: indexPath) as? Self else {
            fatalError("Cell with identifier \(identifier) not found")
        }
        return cell
    }

    static func dequeue(from tableView: UITableView, at indexPath: IndexPath) -> Self {
        let identifier = String(describing: self)

        if tableView.dequeueReusableCell(withIdentifier: identifier) == nil {
            tableView.register(self, forCellReuseIdentifier: identifier)
        }

        guard let cell = tableView.dequeueReusableCell(withIdentifier: identifier, for: indexPath) as? Self else {
            fatalError("Cell with identifier \(identifier) not found")
        }
        
        cell.selectionStyle = .none
        
        return cell
    }
}

